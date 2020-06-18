package com.kevalpatel2106.fxratesample.ui.currencyList

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.di.AppComponent
import javax.inject.Inject

class CurrencyListFragment : Fragment(R.layout.fragment_currency_list) {
    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: CurrencyListViewModel by viewModels { viewModelProvider }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this@CurrencyListFragment)
    }
}
