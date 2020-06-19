package com.kevalpatel2106.fxratesample.ui.currencyList

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.di.AppComponent
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListAdapter
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListActionsListener
import com.kevalpatel2106.fxratesample.utils.nullSafeObserve
import kotlinx.android.synthetic.main.fragment_currency_list.*
import javax.inject.Inject

class CurrencyListFragment
    : Fragment(R.layout.fragment_currency_list),
    CurrencyListActionsListener {

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

        model.currencyListViewStates.nullSafeObserve(viewLifecycleOwner) {
            when (val state = it) {
                CurrencyListViewState.InitialLoad -> handleInitialLoadingState()
                is CurrencyListViewState.UpdateList -> handleUpdateListState(state, adapter)
                is CurrencyListViewState.Error -> handleErrorState(state)
            }
        }
    }

    private fun handleInitialLoadingState() {
        currencyListLoader.isVisible = true
        currencyListRv.isVisible = false
    }

    private fun handleUpdateListState(
        state: CurrencyListViewState.UpdateList,
        adapter: CurrencyListAdapter
    ) {
        currencyListLoader.isVisible = false
        currencyListRv.isVisible = true
        adapter.submitList(state.currencyList)
    }

    private fun handleErrorState(state: CurrencyListViewState.Error) {
        currencyListLoader.isVisible = false
        currencyListRv.isVisible = true
        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setUpRecyclerView(currencyListAdapter: CurrencyListAdapter) {
        currencyListRv.apply {
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
            adapter = currencyListAdapter
        }
    }

    override fun onAmountChanged(code: String, amount: Double) {
        model.onSelectedCurrencyAmountChanged(code, amount)
    }

    override fun onItemSelected(code: String, amount: Double) {
        model.onSelectedCurrencyAmountChanged(code, amount)
    }
}
