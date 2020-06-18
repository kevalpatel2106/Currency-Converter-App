package com.kevalpatel2106.fxratesample.ui.currencyList

import androidx.lifecycle.ViewModel
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CurrencyListViewModel @Inject constructor(
    private val currencyListRepository: CurrencyListRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
