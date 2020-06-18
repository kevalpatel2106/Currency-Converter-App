package com.kevalpatel2106.fxratesample.repo.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET

interface CurrencyListApi {

    @GET("latest?base=EUR")
    fun getListOfBaseCurrency(): Single<CurrencyListDto>

    companion object {
        fun create(retrofit: Retrofit): CurrencyListApi {
            return retrofit.create(CurrencyListApi::class.java)
        }
    }
}
