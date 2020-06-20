package com.kevalpatel2106.fxratesample.ui.currencyList

import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable

sealed class CurrencyListUiState {
    object InitialLoad : CurrencyListUiState()

    data class UpdateList(val currencyList: List<CurrencyListItemRepresentable>) :
        CurrencyListUiState()
}

sealed class ErrorViewState {
    object ShowError : ErrorViewState()
    object HideError : ErrorViewState()
}
