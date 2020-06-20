package com.kevalpatel2106.fxratesample.repo.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class NetworkClientProviderTest {

    private val testBaseUrl = "http://example.com/"
    private val timeOutMins = 10L

    @Test
    @Throws(IOException::class)
    fun checkBaseUrl() {
        val retrofit = NetworkClientProvider(testBaseUrl, timeOutMins)
            .getRetrofitClient()
        assertEquals(testBaseUrl, retrofit.baseUrl().toString())
    }

    @Test
    @Throws(IOException::class)
    fun checkCallAdapterFactories() {
        val callAdapterFactories = NetworkClientProvider(testBaseUrl, timeOutMins)
            .getRetrofitClient()
            .callAdapterFactories()

        assertEquals(1, callAdapterFactories.size)
        assertNotNull(callAdapterFactories.find { it is RxJava2CallAdapterFactory })
    }

    @Test
    @Throws(IOException::class)
    fun checkConverterFactory() {
        val converterFactories = NetworkClientProvider(testBaseUrl, timeOutMins)
            .getRetrofitClient()
            .converterFactories()

        assertEquals(1, converterFactories.size)
        assertNotNull(converterFactories.find { it is MoshiConverterFactory })
    }
}
