package com.kevalpatel2106.fxratesample.ui.currencyList

import com.kevalpatel2106.fxratesample.entity.Currency

object CurrencyListVmUseCase {
    private const val DEFAULT_AMOUNT_IN_BASE_CURRENCY = 1.0

    fun getAmountInBaseCurrency(
        selectedCurrencyData: SelectedCurrencyData?,
        currencies: List<Currency>
    ): Double {
        return when (selectedCurrencyData) {
            null -> DEFAULT_AMOUNT_IN_BASE_CURRENCY
            else -> {
                val selectedCurrencyRate = currencies.find { currency ->
                    currency.code == selectedCurrencyData.code
                }?.rate ?: 0.0
                selectedCurrencyData.amount / selectedCurrencyRate
            }
        }
    }
}
