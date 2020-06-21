package com.kevalpatel2106.fxratesample.di

import com.kevalpatel2106.fxratesample.repo.network.NetoworkConfig
import com.kevalpatel2106.fxratesample.repo.network.NetworkClientProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun provideNetworkClientProvider(): NetworkClientProvider {
        return NetworkClientProvider(baseUrl, NetoworkConfig.TIMEOUT_MINS)
    }
}
