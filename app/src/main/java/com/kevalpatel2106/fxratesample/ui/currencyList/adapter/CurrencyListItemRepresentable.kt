package com.kevalpatel2106.fxratesample.ui.currencyList.adapter

import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.entity.CurrencyCode

data class CurrencyListItemRepresentable(
    val flagUrl: String,
    val name: String,
    val code: CurrencyCode,
    var isSelected: Boolean
) {
    constructor(currency: Currency, isSelected: Boolean) : this(
        flagUrl = currency.flagUrl,
        name = currency.name,
        code = currency.code,
        isSelected = isSelected
    )
}
