package com.kevalpatel2106.fxratesample.repo.network

import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDto
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyListApi {

    /**
     * Load the list of supported currencies and their latest FX rates.
     * Sample API response:
     * <code>
     *     {
     *           "baseCurrency": "EUR",
     *           "rates": {
     *               "AUD": 1.598,
     *               "BGN": 1.97
     *           }
     *       }
     * </code>
     * @property base ISO 4217 currency code for the base currency
     */
    @GET("latest")
    fun getListOfBaseCurrency(@Query("base") base: String): Single<CurrencyListDto>

    companion object {
        fun create(retrofit: Retrofit): CurrencyListApi {
            return retrofit.create(CurrencyListApi::class.java)
        }
    }
}
