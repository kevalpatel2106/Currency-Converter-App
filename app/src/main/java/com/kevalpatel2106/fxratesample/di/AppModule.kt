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

    @Provides
    @Singleton
    fun provideCurrencyListApi(networkClientProvider: NetworkClientProvider): CurrencyListApi {
        return CurrencyListApi.create(networkClientProvider.getRetrofitClient())
    }

    @Singleton
    @Provides
    fun provideCurrencyListRepository(currencyListApi: CurrencyListApi): CurrencyListRepository {
        return CurrencyListRepositoryImpl(
            currencyListApi = currencyListApi,
            currencyListDtoMapper = CurrencyListDtoMapperImpl()
        )
    }
}
