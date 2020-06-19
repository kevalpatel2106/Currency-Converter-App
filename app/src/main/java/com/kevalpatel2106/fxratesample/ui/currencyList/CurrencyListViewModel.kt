package com.kevalpatel2106.fxratesample.ui.currencyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import com.kevalpatel2106.fxratesample.ui.currencyList.CurrencyListVmUseCase.getAmountInBaseCurrency
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable
import com.kevalpatel2106.fxratesample.utils.addTo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyListViewModel @Inject constructor(
    private val currencyListRepository: CurrencyListRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var selectedCurrencyData: SelectedCurrencyData? = null

    private val _displayAmountObservable = BehaviorSubject.create<Map<String, Double>>()
    val displayAmountObservable: Observable<Map<String, Double>> = _displayAmountObservable.hide()

    private val refreshSignal = PublishSubject.create<Unit>()

    private val _currencyListViewStates = MutableLiveData<CurrencyListViewState>(
        CurrencyListViewState.InitialLoad
    )
    val currencyListViewStates: LiveData<CurrencyListViewState> = _currencyListViewStates

    private val _errorViewState = MutableLiveData<ErrorViewState>(ErrorViewState.HideError)
    val errorViewState: LiveData<ErrorViewState> = _errorViewState

    init {
        monitorCurrencyRates()
        refreshSignal.onNext(Unit)
    }

    private fun monitorCurrencyRates() {
        val fxRatesPollingFlowable = currencyListRepository.monitorCurrencyList()
            .doOnError { _errorViewState.value = ErrorViewState.ShowError }
            .retryWhen { it.delay(1, TimeUnit.SECONDS) }

        Flowable.combineLatest(
            refreshSignal.toFlowable(BackpressureStrategy.DROP),
            fxRatesPollingFlowable,
            BiFunction { _: Unit, currencies: List<Currency> -> currencies }
        ).doOnNext(this::publishDisplayAmountChanges)
            .doAfterNext { _errorViewState.value = ErrorViewState.HideError }
            .map(this::convertToListOfAdapterItems)
            .distinctUntilChanged()
            .subscribe({
                _currencyListViewStates.value = CurrencyListViewState.UpdateList(it)
            }, {
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
                    selectedCurrencyData?.amount ?: 0.0
                } else {
                    it.rate * valueInBaseCurrency
                }
        }
        _displayAmountObservable.onNext(hashMap)
    }

    fun onSelectedCurrencyAmountChanged(code: String, amount: Double) {
        selectedCurrencyData = SelectedCurrencyData(code, amount)
        refreshSignal.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
