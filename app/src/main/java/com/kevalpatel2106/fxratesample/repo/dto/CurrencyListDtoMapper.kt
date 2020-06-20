package com.kevalpatel2106.fxratesample.repo.dto

import com.kevalpatel2106.fxratesample.entity.Currency
import java.util.*

/**
 * Mapper that maps [CurrencyListDto] to [List] of [Currency]
 */
interface CurrencyListDtoMapper {
    fun toEntity(dto: CurrencyListDto): List<Currency>
}

class CurrencyListDtoMapperImpl : CurrencyListDtoMapper {

    override fun toEntity(dto: CurrencyListDto): List<Currency> {
        val currencies = mutableListOf<Currency>()
        currencies.add(
            Currency(
                code = dto.baseCurrency,
                name = getNameOfCurrency(dto.baseCurrency),
                rate = FX_RATE_FOR_BASE_CURRENCY,
                flagUrl = getFlagOfCurrency(dto.baseCurrency)
            )
        )

        dto.rates.forEach {
            currencies.add(
                Currency(
                    code = it.key,
                    name = getNameOfCurrency(it.key),
                    rate = it.value,
                    flagUrl = getFlagOfCurrency(it.key)
                )
            )
        }
        return currencies
    }

    private fun getNameOfCurrency(currencyCode: String): String {
        return java.util.Currency.getInstance(currencyCode).displayName
    }

    private fun getFlagOfCurrency(currencyCode: String): String {
        return "${FLAG_BASE_URL}flag_${currencyCode.toLowerCase(Locale.ENGLISH)}.png"
    }

    private companion object {
        private const val FX_RATE_FOR_BASE_CURRENCY = 1.0
        private const val FLAG_BASE_URL =
            "https://raw.githubusercontent.com/midorikocak/currency-picker-android/master/currencypicker/src/main/res/drawable/"
    }
}
