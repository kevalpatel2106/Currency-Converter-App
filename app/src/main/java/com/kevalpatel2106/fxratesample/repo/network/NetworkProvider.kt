package com.kevalpatel2106.fxratesample.repo.network

import com.kevalpatel2106.fxratesample.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


object NetworkProvider {
    private const val READ_TIMEOUT = 1L        // minute
    private const val WRITE_TIMEOUT = 1L       // minute
    private const val CONNECTION_TIMEOUT = 1L  // minute
    private const val BASE_URL = "https://hiring.revolut.codes/api/android/"

    private val okHttpClient = OkHttpClient.Builder()
        .apply {
            readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.MINUTES)
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MINUTES)

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }
        .build()

    private val moshi = Moshi.Builder().add(HashMapMoshiAdapter()).build()

    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
