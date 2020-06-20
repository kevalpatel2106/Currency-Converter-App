package com.kevalpatel2106.fxratesample.ui.currencyList

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.fxratesample.entity.Currency
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyListVmUseCaseTest {
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }

    @Test
    fun `when no currency selected check the amount in base currency is 1`() {
        val selectedCurrencyData: SelectedCurrencyData? = null
        val currencyList = provideListOfCurrencies()

        val amountInBaseCurrency = CurrencyListVmUseCase.getAmountInBaseCurrency(
            selectedCurrencyData = selectedCurrencyData,
            currencies = currencyList
        )
        assertEquals(1.0, amountInBaseCurrency, 0.0)
    }

    @Test
    fun `when selected currency is not in the list check the amount in base currency is 0`() {
        val selectedCurrencyData = SelectedCurrencyData("ABC", 958.0)
        val currencyList = provideListOfCurrencies()

        try {
            CurrencyListVmUseCase.getAmountInBaseCurrency(
                selectedCurrencyData = selectedCurrencyData,
                currencies = currencyList
            )
            fail()
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains(selectedCurrencyData.code) == true)
        }
    }

    @Test
    fun `when selected currency is in the list check the amount in base currency is amount divide by rate`() {
        val currencyList = provideListOfCurrencies()
        val testSelectedAmount = 293.0
        val selectedCurrencyData = SelectedCurrencyData(
            currencyList.first().code,
            testSelectedAmount
        )

        val amountInBaseCurrency = CurrencyListVmUseCase.getAmountInBaseCurrency(
            selectedCurrencyData = selectedCurrencyData,
            currencies = currencyList
        )
        assertEquals(testSelectedAmount / currencyList.first().rate, amountInBaseCurrency, 0.0)
    }

    private fun provideListOfCurrencies() = listOf<Currency>(kFixture(), kFixture(), kFixture())
}
