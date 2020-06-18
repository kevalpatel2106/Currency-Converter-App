package com.kevalpatel2106.fxratesample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.fxratesample.ui.currencyList.CurrencyListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelBindings {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyListViewModel::class)
    internal abstract fun bindCurrencyListViewModel(viewModel: CurrencyListViewModel): ViewModel
}
