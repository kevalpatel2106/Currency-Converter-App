package com.kevalpatel2106.fxratesample.repo.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyListDto(
    @Json(name = "baseCurrency") val baseCurrency: String,
    @Json(name = "rates") val rates: Map<String, Double>
)
