package com.kevalpatel2106.fxratesample.ui.currencyList

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.di.AppComponent
import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListAdapter
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListActionsListener
import com.kevalpatel2106.fxratesample.utils.nullSafeObserve
import kotlinx.android.synthetic.main.fragment_currency_list.*
import javax.inject.Inject

class CurrencyListFragment
    : Fragment(R.layout.fragment_currency_list), CurrencyListActionsListener {

    private val errorSnackbar by lazy {
        Snackbar.make(currencyListRootContainer, R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
    }

    @Inject
    internal lateinit var viewModelProvider: ViewModelProvider.Factory

    private val model: CurrencyListViewModel by viewModels { viewModelProvider }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this@CurrencyListFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CurrencyListAdapter(this, model.displayAmountObservable)

        setUpRecyclerView(adapter)

        model.currencyListUiStates.nullSafeObserve(viewLifecycleOwner) {
            when (val state = it) {
                CurrencyListUiState.InitialLoad -> handleInitialLoadingState()
                is CurrencyListUiState.UpdateList -> handleUpdateListState(state, adapter)
            }
        }
        observeErrorStates()
    }

    private fun observeErrorStates() {
        model.errorViewState.nullSafeObserve(viewLifecycleOwner) {
            when (it) {
                ErrorViewState.HideError -> {
                    if (errorSnackbar.isShownOrQueued) errorSnackbar.dismiss()
                }
                ErrorViewState.ShowError -> {
                    if (!errorSnackbar.isShownOrQueued) errorSnackbar.show()
                }
            }
        }
    }

    private fun handleInitialLoadingState() {
        currencyListLoader.isVisible = true
        currencyListRv.isVisible = false
    }

    private fun handleUpdateListState(
        state: CurrencyListUiState.UpdateList,
        adapter: CurrencyListAdapter
    ) {
        currencyListLoader.isVisible = false
        currencyListRv.isVisible = true
        adapter.submitList(state.currencyList)
    }

    private fun setUpRecyclerView(currencyListAdapter: CurrencyListAdapter) {
        currencyListRv.apply {
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
            adapter = currencyListAdapter
        }
    }

    override fun onAmountChanged(code: CurrencyCode, amount: Amount) {
        model.onSelectedCurrencyAmountChanged(code, amount)
    }

    override fun onItemFocused(code: CurrencyCode, amount: Amount) {
        model.onSelectedCurrencyAmountChanged(code, amount)
    }

    override fun onRowClicked(code: CurrencyCode, amount: Amount) {
        model.onSelectedCurrencyAmountChanged(code, amount)
        currencyListRv.postDelayed({ currencyListRv.smoothScrollToPosition(0) }, 500)
    }
}
