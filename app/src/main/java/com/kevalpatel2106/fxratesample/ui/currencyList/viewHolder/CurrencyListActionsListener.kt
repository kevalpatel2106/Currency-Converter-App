package com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder

import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode

interface CurrencyListActionsListener {

    fun onItemSelected(code: CurrencyCode, amount: Amount)

    fun onAmountChanged(code: CurrencyCode, amount: Amount)
}
