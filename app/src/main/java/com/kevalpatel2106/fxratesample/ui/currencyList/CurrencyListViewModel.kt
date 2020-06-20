package com.kevalpatel2106.fxratesample.ui.currencyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import com.kevalpatel2106.fxratesample.ui.currencyList.CurrencyListVmUseCase.getAmountInBaseCurrency
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable
import com.kevalpatel2106.fxratesample.utils.addTo
import com.kevalpatel2106.fxratesample.utils.emptyAmount
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class CurrencyListViewModel @Inject constructor(
    private val currencyListRepository: CurrencyListRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var selectedCurrencyData: SelectedCurrencyData? = null

    private val _displayAmountObservable = BehaviorSubject.create<Map<CurrencyCode, Amount>>()
    val displayAmountObservable: Observable<Map<CurrencyCode, Amount>> =
        _displayAmountObservable.hide()

    private val refreshSignal = PublishSubject.create<Unit>()

    private val _currencyListUiStates = MutableLiveData<CurrencyListUiState>(
        CurrencyListUiState.InitialLoad
    )
    val currencyListUiStates: LiveData<CurrencyListUiState> = _currencyListUiStates

    private val _errorViewState = MutableLiveData<ErrorViewState>(ErrorViewState.HideError)
    val errorViewState: LiveData<ErrorViewState> = _errorViewState

    init {
        monitorCurrencyRates()
        refreshSignal.onNext(Unit)
    }

    private fun monitorCurrencyRates() {
        val fxRatesPollingFlowable = currencyListRepository.monitorCurrencyList(
            doOnError = Consumer { _errorViewState.postValue(ErrorViewState.ShowError) }
        )

        Flowable.combineLatest(
            refreshSignal.toFlowable(BackpressureStrategy.DROP),
            fxRatesPollingFlowable,
            BiFunction { _: Unit, currencies: List<Currency> -> currencies }
        ).doOnNext(this::publishDisplayAmountChanges)
            .doAfterNext { _errorViewState.value = ErrorViewState.HideError }
            .map(this::convertToListOfAdapterItems)
            .distinctUntilChanged()
            .subscribe({
                _currencyListUiStates.value = CurrencyListUiState.UpdateList(it)
            }, {
                it.printStackTrace()
                _errorViewState.value = ErrorViewState.ShowError
            })
            .addTo(compositeDisposable)
    }

    private fun convertToListOfAdapterItems(currencies: List<Currency>): List<CurrencyListItemRepresentable> {
        return currencies.map {
            CurrencyListItemRepresentable(
                currency = it,
                isSelected = selectedCurrencyData?.code == it.code
            )
        }.sortedByDescending { it.isSelected }
    }

    private fun publishDisplayAmountChanges(updatedCurrencies: List<Currency>) {
        val valueInBaseCurrency = getAmountInBaseCurrency(selectedCurrencyData, updatedCurrencies)
        val hashMap = hashMapOf<String, Double>()
        updatedCurrencies.forEach {
            hashMap[it.code] =
                if (selectedCurrencyData != null && it.code == selectedCurrencyData?.code) {
                    selectedCurrencyData?.amount ?: emptyAmount()
                } else {
                    it.rate * valueInBaseCurrency
                }
        }
        _displayAmountObservable.onNext(hashMap)
    }

    fun onSelectedCurrencyAmountChanged(code: CurrencyCode, amount: Amount) {
        selectedCurrencyData = SelectedCurrencyData(code, amount)
        refreshSignal.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
