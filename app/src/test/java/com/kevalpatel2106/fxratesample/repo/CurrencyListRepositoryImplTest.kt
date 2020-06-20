package com.kevalpatel2106.fxratesample.repo

import com.flextrade.kfixture.KFixture
import com.flextrade.kfixture.customisation.IgnoreDefaultArgsConstructorCustomisation
import com.kevalpatel2106.fxratesample.RxSchedulersOverrideRule
import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDto
import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDtoMapper
import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.functions.Consumer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class CurrencyListRepositoryImplTest {

    @JvmField
    @Rule
    val rule1 = RxSchedulersOverrideRule()

    @Mock
    lateinit var currencyListApi: CurrencyListApi

    @Captor
    lateinit var baseCurrencyCaptor: ArgumentCaptor<String>

    @Mock
    lateinit var currencyListDtoMapper: CurrencyListDtoMapper

    @Mock
    lateinit var mockConsumer: Consumer<Throwable>

    private val mockCurrencyDto = CurrencyListDto(
        baseCurrency = "EUR",
        rates = hashMapOf(
            "GBP" to 1.23,
            "INR" to 80.91
        )
    )
    private val kFixture: KFixture = KFixture { add(IgnoreDefaultArgsConstructorCustomisation()) }
    private lateinit var repository: CurrencyListRepository
    private val mockCurrenciesList = listOf<Currency>(kFixture(), kFixture(), kFixture())

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(currencyListDtoMapper.toEntity(mockCurrencyDto)).thenReturn(mockCurrenciesList)

        repository = CurrencyListRepositoryImpl(currencyListApi, currencyListDtoMapper)
    }

    @Test
    fun `given currency list api success when monitor currency list called check observable not terminated`() {
        whenever(currencyListApi.getListOfBaseCurrency(anyString()))
            .thenReturn(Single.just(mockCurrencyDto))

        val testObservable = repository.monitorCurrencyList(doOnError = mockConsumer).test()

        testObservable.assertNoErrors()
            .assertNotComplete()
            .assertNotTerminated()
    }

    @Test
    fun `given currency list api error  when monitor currency list called check observable not terminated`() {
        val errorMessage = kFixture<String>()
        whenever(currencyListApi.getListOfBaseCurrency(anyString()))
            .thenReturn(Single.error(Throwable(errorMessage)))

        val testObservable = repository.monitorCurrencyList(doOnError = mockConsumer).test()

        testObservable.assertNoErrors()
            .assertNotComplete()
            .assertNotTerminated()
    }

    @Test
    fun `given currency list api error when monitor currency list called check do on error executed`() {
        val error = Throwable(kFixture<String>())
        whenever(currencyListApi.getListOfBaseCurrency(anyString())).thenReturn(Single.error(error))

        repository.monitorCurrencyList(doOnError = mockConsumer).test()

        verify(mockConsumer).accept(error)
    }

    @Test
    fun `given currency list api success when monitor currency list called check it keeps polling every second`() {
        val expectedPollDurationMills = 1000L
        val waitingDurationMills = 3 * expectedPollDurationMills + 100 /* Delta */
        whenever(currencyListApi.getListOfBaseCurrency(anyString()))
            .thenReturn(Single.just(mockCurrencyDto))

        val testSubscriber = repository.monitorCurrencyList(doOnError = mockConsumer).test()
        testSubscriber.await(waitingDurationMills, TimeUnit.MILLISECONDS)

        testSubscriber.assertValueCount((waitingDurationMills / expectedPollDurationMills).toInt() + 1 /* Initial subscribe call */)
    }

    @Test
    fun `given currency list api error when monitor currency list called check it keeps retrying every second`() {
        val expectedRetryDurationMills = 1000L
        val waitingDurationMills = 3 * expectedRetryDurationMills + 100 /* Delta */
        whenever(currencyListApi.getListOfBaseCurrency(anyString()))
            .thenReturn(Single.error(Throwable()))

        val testSubscriber = repository.monitorCurrencyList(doOnError = mockConsumer).test()
        testSubscriber.await(waitingDurationMills, TimeUnit.MILLISECONDS)

        verify(
            mockConsumer,
            times((waitingDurationMills / expectedRetryDurationMills).toInt() + 1 /* Initial subscribe call */)
        ).accept(any())
    }

}
