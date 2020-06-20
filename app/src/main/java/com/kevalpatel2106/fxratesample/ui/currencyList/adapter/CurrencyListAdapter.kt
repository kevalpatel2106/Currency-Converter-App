package com.kevalpatel2106.fxratesample.ui.currencyList.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListActionsListener
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListViewHolder
import io.reactivex.Observable

class CurrencyListAdapter(
    private val listener: CurrencyListActionsListener,
    private val displayAmountObservable: Observable<Map<CurrencyCode, Amount>>
) : ListAdapter<CurrencyListItemRepresentable, CurrencyListViewHolder>(
    CurrencyListAdapterDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyListViewHolder.create(
            parent = parent,
            displayAmountObservable = displayAmountObservable,
            listener = listener
        )

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) =
        holder.bind(getItem(position))
}