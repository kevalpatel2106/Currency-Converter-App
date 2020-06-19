package com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder

interface CurrencyListActionsListener {

    fun onItemSelected(code: String, amount: Double)

    fun onAmountChanged(code: String, amount: Double)
}
