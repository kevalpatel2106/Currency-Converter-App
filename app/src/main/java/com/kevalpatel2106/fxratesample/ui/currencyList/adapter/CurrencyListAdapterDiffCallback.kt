package com.kevalpatel2106.fxratesample.ui.currencyList.adapter

import androidx.recyclerview.widget.DiffUtil

internal object CurrencyListAdapterDiffCallback :
    DiffUtil.ItemCallback<CurrencyListItemRepresentable>() {

    override fun areContentsTheSame(
        oldItem: CurrencyListItemRepresentable,
        newItem: CurrencyListItemRepresentable
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: CurrencyListItemRepresentable,
        newItem: CurrencyListItemRepresentable
    ): Boolean {
        return oldItem.code == newItem.code
    }
}
