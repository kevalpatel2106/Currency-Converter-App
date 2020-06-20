package com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable
import com.kevalpatel2106.fxratesample.utils.FxRatesEditText
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_currency.*

class CurrencyListViewHolder(
    override val containerView: View,
    private val displayAmountObservable: Observable<Map<CurrencyCode, Amount>>,
    private val listener: CurrencyListActionsListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer,
    FxRatesEditText.AmountInputChangedListener {

    fun bind(item: CurrencyListItemRepresentable) {
        listItemCurrencyCodeTv.text = item.code
        listItemCurrencyNameTv.text = item.name

        listItemCurrencyValueEt.apply {
            if (item.isSelected) makeEditable(item) else makeReadOnly(item)
        }

        Glide.with(containerView.context)
            .load(item.flagUrl)
            .circleCrop()
            .into(listItemCurrencyFlagIv)

        listItemCurrencyRootContainer.setOnClickListener {
            listener.onItemSelected(item.code, listItemCurrencyValueEt.getAmount())
        }
    }

    private fun FxRatesEditText.makeReadOnly(item: CurrencyListItemRepresentable) {
        setUpReadOnly(item.code, displayAmountObservable)
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) listener.onItemSelected(item.code, listItemCurrencyValueEt.getAmount())
        }
    }

    private fun FxRatesEditText.makeEditable(item: CurrencyListItemRepresentable) {
        setUpEditable(item.code, this@CurrencyListViewHolder, displayAmountObservable)
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener.onAmountChanged(item.code, listItemCurrencyValueEt.getAmount())
            }
            false
        }
    }

    override fun onAmountChanged(newAmount: Double) {
        listener.onAmountChanged(listItemCurrencyCodeTv.text.toString(), newAmount)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            displayAmountObservable: Observable<Map<CurrencyCode, Amount>>,
            listener: CurrencyListActionsListener
        ): CurrencyListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_currency, parent, false)
            return CurrencyListViewHolder(
                view,
                displayAmountObservable,
                listener
            )
        }
    }
}
