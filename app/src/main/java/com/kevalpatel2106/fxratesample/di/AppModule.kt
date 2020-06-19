package com.kevalpatel2106.fxratesample.di

import com.kevalpatel2106.fxratesample.repo.CurrencyListRepository
import com.kevalpatel2106.fxratesample.repo.CurrencyListRepositoryImpl
import com.kevalpatel2106.fxratesample.repo.dto.CurrencyListDtoMapperImpl
import com.kevalpatel2106.fxratesample.repo.network.CurrencyListApi
import com.kevalpatel2106.fxratesample.repo.network.NetworkClientProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideCurrencyListRepository(): CurrencyListRepository {
        return CurrencyListRepositoryImpl(
            currencyListApi = CurrencyListApi.create(NetworkClientProvider.getRetrofitClient()),
            currencyListDtoMapper = CurrencyListDtoMapperImpl()
        )
    }
}
