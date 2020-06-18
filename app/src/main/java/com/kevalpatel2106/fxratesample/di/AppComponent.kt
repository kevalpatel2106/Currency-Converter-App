package com.kevalpatel2106.fxratesample.di

import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.kevalpatel2106.fxratesample.ui.currencyList.CurrencyListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelBindings::class])
interface AppComponent {
    fun provideCurrencyListApiService(): CurrencyListApi

    fun inject(currencyListFragment: CurrencyListFragment)

    companion object {
        fun get(): AppComponent = DaggerAppComponent.builder().appModule(AppModule()).build()
    }
}
