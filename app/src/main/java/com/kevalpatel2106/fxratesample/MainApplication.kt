package com.kevalpatel2106.fxratesample

import android.app.Application
import com.kevalpatel2106.fxratesample.di.AppComponent
import com.kevalpatel2106.fxratesample.di.AppModule
import com.kevalpatel2106.fxratesample.di.DaggerAppComponent
import com.kevalpatel2106.fxratesample.di.NetworkModule
import com.kevalpatel2106.fxratesample.repo.network.NetoworkConfig

interface FxRatesApplication {
    fun getAppComponent(): AppComponent
}

class MainApplication : Application(), FxRatesApplication {
    override fun getAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .networkModule(NetworkModule(NetoworkConfig.BASE_URL))
            .appModule(AppModule())
            .build()
    }
}
