package com.kevalpatel2106.fxratesample.ui.currencyList

import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.utils.emptyAmount

object CurrencyListVmUseCase {
    private const val DEFAULT_AMOUNT_IN_BASE_CURRENCY: Amount = 1.0

    fun getAmountInBaseCurrency(
        selectedCurrencyData: SelectedCurrencyData?,
        currencies: List<Currency>
    ): Amount {
        return when (selectedCurrencyData) {
            null -> DEFAULT_AMOUNT_IN_BASE_CURRENCY
            else -> {
                val selectedCurrencyRate = currencies.find { currency ->
                    currency.code == selectedCurrencyData.code
                }?.rate ?: emptyAmount()
                selectedCurrencyData.amount / selectedCurrencyRate
            }
        }
    }
}
