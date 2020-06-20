package com.kevalpatel2106.fxratesample.entity

data class Currency(
    val flagUrl: String,
    val name: String,
    val code: CurrencyCode,
    val rate: Amount // Rate is compare to base currency
)
