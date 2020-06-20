package com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder

import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode

/**
 * Listener interface that notifies about the view events in the currency list to the subscriber.
 */
interface CurrencyListActionsListener {

    /**
     * Callback that notifies when the list row is clicked.
     *
     * @param code [CurrencyCode] for the row clicked
     * @param amount [Amount] displayed in the row clicked.
     */
    fun onRowClicked(code: CurrencyCode, amount: Amount)

    /**
     * Callback that notifies when edit text in any list item gets focus (i.e. user taps on it).
     *
     * @param code [CurrencyCode] for the row focused
     * @param amount [Amount] displayed in the row focused.
     */
    fun onItemFocused(code: CurrencyCode, amount: Amount)

    /**
     * Callback that notifies when the amount displayed in the selected row changes.
     *
     * @param code [CurrencyCode] for the list item for witch amount is changed
     * @param amount Changed [Amount].
     */
    fun onAmountChanged(code: CurrencyCode, amount: Amount)
}
