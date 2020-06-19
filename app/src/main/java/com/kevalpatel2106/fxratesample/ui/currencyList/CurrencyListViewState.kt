package com.kevalpatel2106.fxratesample.ui.currencyList

import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable

sealed class CurrencyListViewState {
    object InitialLoad : CurrencyListViewState()

    data class UpdateList(val currencyList: List<CurrencyListItemRepresentable>) :
        CurrencyListViewState()
}

sealed class ErrorViewState {
    object ShowError : ErrorViewState()
    object HideError : ErrorViewState()
}
