package com.kevalpatel2106.fxratesample.repo.network

import com.kevalpatel2106.fxratesample.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class NetworkClientProvider(private val baseUrl: String, private val timeOutMins: Long) {
    private val okHttpClient = OkHttpClient.Builder()
        .apply {
            readTimeout(timeOutMins, TimeUnit.MINUTES)
            writeTimeout(timeOutMins, TimeUnit.MINUTES)
            connectTimeout(timeOutMins, TimeUnit.MINUTES)

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }
        .build()

    private val moshi = Moshi.Builder().add(HashMapMoshiAdapter()).build()

    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}
