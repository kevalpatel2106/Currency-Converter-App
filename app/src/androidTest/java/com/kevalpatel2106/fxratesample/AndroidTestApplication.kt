package com.kevalpatel2106.fxratesample

import android.app.Application
import com.kevalpatel2106.fxratesample.di.AppComponent
import com.kevalpatel2106.fxratesample.di.AppModule
import com.kevalpatel2106.fxratesample.di.DaggerAppComponent
import com.kevalpatel2106.fxratesample.di.NetworkModule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.io.IOException
import java.net.HttpURLConnection

class AndroidTestApplication : Application(), FxRatesApplication {
    private val apiResponse = """
        {"baseCurrency":"EUR","rates":{"AUD":1.594,"BGN":1.982,"BRL":4.259,"CAD":1.517,"CHF":1.144,"CNY":7.724,"CZK":25.967,"DKK":7.549,"GBP":0.889,"HKD":9.015,"HRK":7.51,"HUF":323.06,"IDR":15980.225,"ILS":4.128,"INR":80.9,"ISK":134.424,"JPY":126.414,"KRW":1293.662,"MXN":21.799,"MYR":4.686,"NOK":9.892,"NZD":1.655,"PHP":60.232,"PLN":4.396,"RON":4.763,"RUB":74.942,"SEK":10.646,"SGD":1.556,"THB":35.912,"USD":1.135,"ZAR":16.084}}
    """.trimIndent()

    private var mockWebServer = MockWebServer()
    private lateinit var baseUrl: String

    override fun onCreate() {
        super.onCreate()

        // Start the mock server
        val thread = object : Thread() {
            override fun run() {
                try {
                    mockWebServer.start()
                    baseUrl = mockWebServer.url("/").toString()
                    enqueueResponse()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        thread.start()

        // Wait for the server to start
        Thread.sleep(1000L)
    }

    private fun enqueueResponse() {
        for (x in 0..20) {
            mockWebServer.enqueue(
                MockResponse()
                    .setHeader("Content-type", "application/json")
                    .setBody(apiResponse)
                    .setResponseCode(HttpURLConnection.HTTP_OK)
            )
        }
    }

    override fun getAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .networkModule(NetworkModule(baseUrl))
            .appModule(AppModule())
            .build()
    }
}
