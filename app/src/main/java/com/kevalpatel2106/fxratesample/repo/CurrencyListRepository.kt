package com.kevalpatel2106.fxratesample.repo

import com.kevalpatel2106.fxratesample.entity.Currency
import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDtoMapper
import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Repository that monitors the currency list and their latest FX rates
 */
interface CurrencyListRepository {

    /**
     * This method monitors the currency list and their latest FX rates. It keeps polling server
     * for latest rates every [intervalInMills] milli seconds (default value is [FX_RATE_UPDATE_INTERVAL]
     * and notifies caller about any error through [doOnError] callback. If any error occurs this
     * function will recover by itself and keeps polling server for latest FX rates
     * NOTE: The frequency of FX rates updates is not guarantied to occur every [intervalInMills].
     *
     * @return [Flowable] that emits the [List] of [Currency] whenever latest FX rates are available.
     */
    fun monitorCurrencyList(
        baseCurrency: String = BASE_CURRENCY,
        intervalInMills: Long = FX_RATE_UPDATE_INTERVAL,
        doOnError: Consumer<Throwable>
    ): Flowable<List<Currency>>

    companion object {
        private const val BASE_CURRENCY = "EUR"
        private const val FX_RATE_UPDATE_INTERVAL = 1000L  // MILLISECONDS
    }
}

/**
 * Implementation of [CurrencyListRepository]
 */
class CurrencyListRepositoryImpl @Inject constructor(
    private val currencyListApi: CurrencyListApi,
    private val currencyListDtoMapper: CurrencyListDtoMapper
) : CurrencyListRepository {

    /**
     * @see CurrencyListRepository.monitorCurrencyList
     */
    override fun monitorCurrencyList(
        baseCurrency: String,
        intervalInMills: Long,
        doOnError: Consumer<Throwable>
    ): Flowable<List<Currency>> {
        return currencyListApi.getListOfBaseCurrency(baseCurrency)
            .repeatWhen { it.delay(intervalInMills, TimeUnit.MILLISECONDS) }
            .doOnError(doOnError)
            .retryWhen { it.delay(1, TimeUnit.SECONDS) }
            .map(currencyListDtoMapper::toEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
