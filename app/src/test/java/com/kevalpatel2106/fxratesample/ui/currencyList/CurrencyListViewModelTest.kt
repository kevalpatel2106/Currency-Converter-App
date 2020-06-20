package com.kevalpatel2106.fxratesample.ui.currencyList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.fxratesample.RxSchedulersOverrideRule
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.getOrAwaitValue
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
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
    }

    @Test
    fun `when viewmodel initialised check initial state of currency list viewstate`() {
        // when
        initViewModel(true)

        // then
        assert(model.currencyListUiStates.value is CurrencyListUiState.InitialLoad)
    }

    @Test
    fun `when list viewmodel initialised check initial state of error viewstate`() {
        // when
        initViewModel(true)

        // then
        assert(model.errorViewState.value is ErrorViewState.HideError)
    }

    @Test
    fun `when view model initialised check server polling started`() {
        // when
        initViewModel(true)

        // then
        verify(mockRepo).monitorCurrencyList(anyString(), anyLong(), any())
    }

    @Test
    fun `given list of currencies when api returns success check currency list view state`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()

        // when
        fxRatesSubject.onNext(currencies)

        // then
        assert(model.currencyListUiStates.getOrAwaitValue() is CurrencyListUiState.UpdateList)
    }

    @Test
    fun `given list of currencies and no currency selected when api returns success check adapter items list`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()

        // when
        fxRatesSubject.onNext(currencies)

        // then
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
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()
        val selectedCurrency = currencies.first()
        model.onSelectedCurrencyAmountChanged(selectedCurrency.code, kFixture())

        // when
        fxRatesSubject.onNext(currencies)

        // then
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

    @Test
    fun `given list of currencies when api returns success check error state is hide error`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()

        // when
        fxRatesSubject.onNext(currencies)

        // then
        val value = model.errorViewState.getOrAwaitValue()
        assertTrue(value is ErrorViewState.HideError)
    }

    @Test
    fun `when api returns error check error state changes to show`() {
        // when
        initViewModel(false)

        // then
        val value = model.errorViewState.getOrAwaitValue()
        assertTrue(value is ErrorViewState.ShowError)
    }

    @Test
    fun `given list of currency when api returns success check if display amount changes emitted`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()

        // when
        fxRatesSubject.onNext(currencies)

        // then
        val testSubscriber = model.displayAmountObservable.test()
        testSubscriber.assertNoErrors()
            .assertNotComplete()
            .assertNotTerminated()
            .assertValueCount(1)
            .assertValueAt(0) { it.size == currencies.size }
    }

    @Test
    fun `given list of currencies and no currency selected when api returns success check display amount`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()

        // when
        fxRatesSubject.onNext(currencies)

        // then
        val testSubscriber = model.displayAmountObservable.test()
        testSubscriber.values().first().forEach { (key, value) ->
            assertEquals(value, currencies.find { it.code == key }?.rate)
        }
    }

    @Test
    fun `given list of currencies and currency selected when api returns success check display amount`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()
        val selectedCurrencyData = SelectedCurrencyData(currencies.first().code, kFixture())
        val valueInBaseCurrency = CurrencyListVmUseCase.getAmountInBaseCurrency(
            selectedCurrencyData,
            currencies
        )
        model.onSelectedCurrencyAmountChanged(
            selectedCurrencyData.code,
            selectedCurrencyData.amount
        )

        // when
        fxRatesSubject.onNext(currencies)

        // then
        val testSubscriber = model.displayAmountObservable.test()
        testSubscriber.values().first().forEach { (key, value) ->
            assertEquals(
                value,
                (currencies.find { it.code == key }!!.rate.times(valueInBaseCurrency)),
                0.0
            )
        }
    }


    @Test
    fun `given list of currency and currency selected when api returns success check if selected currency is first in list`() {
        // given
        initViewModel(true)
        val currencies = provideListOfCurrencies()
        fxRatesSubject.onNext(currencies)

        val selectedCurrencyData = SelectedCurrencyData(currencies.last().code, kFixture())

        // when
        model.onSelectedCurrencyAmountChanged(
            selectedCurrencyData.code,
            selectedCurrencyData.amount
        )

        // then
        val value = model.currencyListUiStates.getOrAwaitValue() as CurrencyListUiState.UpdateList
        assertTrue(value.currencyList.first().isSelected)
    }


    private fun initViewModel(apiShouldSuccess: Boolean = true) {
        if (apiShouldSuccess)
            whenever(mockRepo.monitorCurrencyList(anyString(), anyLong(), any()))
                .thenReturn(fxRatesSubject.toFlowable(BackpressureStrategy.DROP))
        else
            whenever(mockRepo.monitorCurrencyList(anyString(), anyLong(), any()))
                .thenReturn(Flowable.error(Throwable()))

        model = CurrencyListViewModel(mockRepo)
    }

    private fun provideListOfCurrencies() = listOf<Currency>(kFixture(), kFixture(), kFixture())
}
