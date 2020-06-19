package com.kevalpatel2106.fxratesample.ui.currencyList

import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable

sealed class CurrencyListViewState {
    object InitialLoad : CurrencyListViewState()

    data class UpdateList(val currencyList: List<CurrencyListItemRepresentable>) :
        CurrencyListViewState()

    data class Error(val errorMessage: String) : CurrencyListViewState()
}
