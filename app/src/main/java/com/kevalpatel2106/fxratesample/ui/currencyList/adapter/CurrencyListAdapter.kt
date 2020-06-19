package com.kevalpatel2106.fxratesample.ui.currencyList.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListActionsListener
import com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder.CurrencyListViewHolder
import io.reactivex.Observable

class CurrencyListAdapter(
    private val listener: CurrencyListActionsListener,
    private val amountToDisplayObservable: Observable<Map<String, Double>>
) : ListAdapter<CurrencyListItemRepresentable, CurrencyListViewHolder>(
    CurrencyListAdapterDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyListViewHolder.create(
            parent = parent,
            amountToDisplayObservable = amountToDisplayObservable,
            listener = listener
        )

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) =
        holder.bind(getItem(position))
}
