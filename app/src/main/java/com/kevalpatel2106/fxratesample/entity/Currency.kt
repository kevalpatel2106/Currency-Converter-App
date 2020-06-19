package com.kevalpatel2106.fxratesample.entity

data class Currency(
    val flagUrl: String,
    val name: String,
    val code: String,
    val rate: Double // Rate is compare to base currency
)
