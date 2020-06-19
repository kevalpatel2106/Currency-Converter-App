package com.kevalpatel2106.fxratesample.repo.network

import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDto
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyListApi {

    @GET("latest")
    fun getListOfBaseCurrency(@Query("base") base: String): Single<CurrencyListDto>

    companion object {
        fun create(retrofit: Retrofit): CurrencyListApi {
            return retrofit.create(CurrencyListApi::class.java)
        }
    }
}
