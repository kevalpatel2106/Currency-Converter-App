package com.kevalpatel2106.fxratesample.di

import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.kevalpatel2106.fxratesample.repo.network.NetworkProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideCurrencyListApiService(): CurrencyListApi {
        return CurrencyListApi.create(NetworkProvider.getRetrofitClient())
    }
}
