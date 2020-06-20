package com.kevalpatel2106.fxratesample.repo.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for latest currency rates API
 */
@JsonClass(generateAdapter = true)
data class CurrencyListDto(
    @Json(name = "baseCurrency") val baseCurrency: String,
    @Json(name = "rates") val rates: Map<String, Double>
)
