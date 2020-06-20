package com.kevalpatel2106.fxratesample.ui.currencyList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.fxratesample.RxSchedulersOverrideRule
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.getOrAwaitValue
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class CurrencyListViewModelTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var mockRepo: CurrencyListRepository

    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private val fxRatesSubject = PublishSubject.create<List<Currency>>()

    private lateinit var model: CurrencyListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(mockRepo.monitorCurrencyList())
            .thenReturn(fxRatesSubject.toFlowable(BackpressureStrategy.DROP))

        model = CurrencyListViewModel(mockRepo)
    }

    @Test
    fun `when viewmodel initialised check initial state of currency list viewstate`() {
        assert(model.currencyListUiStates.value is CurrencyListUiState.InitialLoad)
    }

    @Test
    fun `when list viewmodel initialised check initial state of error viewstate`() {
        assert(model.errorViewState.value is ErrorViewState.HideError)
    }

    @Test
    fun `when view model initialised check server polling started`() {
        Mockito.verify(mockRepo).monitorCurrencyList()
    }

    @Test
    fun `given list of currencies when api returns success check currency list view state`() {
        val currencies = provideListOfCurrencies()

        fxRatesSubject.onNext(currencies)

        assert(model.currencyListUiStates.getOrAwaitValue() is CurrencyListUiState.UpdateList)
    }

    @Test
    fun `given list of currencies and no currency selected when api returns success check adapter items list`() {
        val currencies = provideListOfCurrencies()

        fxRatesSubject.onNext(currencies)

        val value =
            model.currencyListUiStates.getOrAwaitValue() as CurrencyListUiState.UpdateList
        value.currencyList.forEach { adapterItem ->
            val equivalentCurrency = currencies.find { it.code == adapterItem.code }
            assertEquals(equivalentCurrency?.flagUrl, adapterItem.flagUrl)
            assertEquals(equivalentCurrency?.name, adapterItem.name)
            assertEquals(equivalentCurrency?.code, adapterItem.code)
            assertFalse(adapterItem.isSelected)
        }
    }

    @Test
    fun `given list of currencies and currency selected when api returns success check adapter items list`() {
        val currencies = provideListOfCurrencies()
        val selectedCurrency = currencies.first()
        model.onSelectedCurrencyAmountChanged(selectedCurrency.code, kFixture())

        fxRatesSubject.onNext(currencies)

        val value =
            model.currencyListUiStates.getOrAwaitValue() as CurrencyListUiState.UpdateList
        value.currencyList.forEach { adapterItem ->
            val equivalentCurrency = currencies.find { it.code == adapterItem.code }
            assertEquals(equivalentCurrency?.flagUrl, adapterItem.flagUrl)
            assertEquals(equivalentCurrency?.name, adapterItem.name)
            assertEquals(equivalentCurrency?.code, adapterItem.code)
            assertEquals(selectedCurrency.code == adapterItem.code, adapterItem.isSelected)
        }
    }

    private fun provideListOfCurrencies() = listOf<Currency>(kFixture(), kFixture(), kFixture())
}
