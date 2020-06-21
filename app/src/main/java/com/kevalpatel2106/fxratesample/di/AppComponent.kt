package com.kevalpatel2106.fxratesample.di

import android.content.Context
import com.kevalpatel2106.fxratesample.FxRatesApplication
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.kevalpatel2106.fxratesample.repo.network.NetworkClientProvider
import com.kevalpatel2106.fxratesample.ui.currencyList.CurrencyListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelBindings::class])
interface AppComponent {
    fun provideNetworkClientProvider(): NetworkClientProvider
    fun provideCurrencyListApi(): CurrencyListApi
    fun provideCurrencyListRepository(): CurrencyListRepository

    fun inject(currencyListFragment: CurrencyListFragment)

    companion object {
        fun get(context: Context): AppComponent = (context as FxRatesApplication).getAppComponent()
    }
}
