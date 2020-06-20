package com.kevalpatel2106.fxratesample.repo.dto

import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class CurrencyListDtoMapperImplTest {
    private val baseCurrencyCode = "EUR"
    private val mapOfCurrency: HashMap<CurrencyCode, Amount> = hashMapOf(
        "GBP" to 1.23,
        "INR" to 80.91
    )
    private val mockCurrencyDto = CurrencyListDto(
        baseCurrency = baseCurrencyCode,
        rates = mapOfCurrency
    )

    @Test
    fun `given currency dto when converting to entity check size of the list`() {
        val currencies = CurrencyListDtoMapperImpl().toEntity(mockCurrencyDto)
        assertEquals(mapOfCurrency.size + 1, currencies.size)
    }

    @Test
    fun `given currency dto when converting to entity check base currency added at top`() {
        val currencies = CurrencyListDtoMapperImpl().toEntity(mockCurrencyDto)
        assertEquals(baseCurrencyCode, currencies.first().code)
    }

    @Test
    fun `given currency dto when converting to entity check base currency rate is 1`() {
        val currencies = CurrencyListDtoMapperImpl().toEntity(mockCurrencyDto)
        assertEquals(1.0, currencies.first().rate, 0.0)
    }

    @Test
    fun `given currency dto when converting to entity check other currency rates`() {
        val currencies = CurrencyListDtoMapperImpl().toEntity(mockCurrencyDto)
        mapOfCurrency.forEach { (code, rate) ->
            val found = currencies.firstOrNull { it.code == code }
            assertEquals(rate, found?.rate)
        }
    }

    @Test
    fun `given currency dto when converting to entity check other currency names`() {
        val currencies = CurrencyListDtoMapperImpl().toEntity(mockCurrencyDto)
        mapOfCurrency.forEach { (code, _) ->
            val found = currencies.firstOrNull { it.code == code }
            assertEquals(Currency.getInstance(code).displayName, found?.name)
        }
    }
}
