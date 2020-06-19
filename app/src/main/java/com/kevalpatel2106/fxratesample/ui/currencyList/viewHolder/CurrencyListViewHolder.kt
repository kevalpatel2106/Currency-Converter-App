package com.kevalpatel2106.fxratesample.ui.currencyList.viewHolder

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.ui.currencyList.adapter.CurrencyListItemRepresentable
import com.kevalpatel2106.fxratesample.utils.FxRatesEditText
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_currency.*

class CurrencyListViewHolder(
    override val containerView: View,
    private val amountToDisplayObservable: Observable<Map<String, Double>>,
    private val listener: CurrencyListActionsListener
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isBlank()) return
            listener.onAmountChanged(
                listItemCurrencyCodeTv.text.toString(),
                s.toString().toDouble()
            )
        }
    }

    fun bind(item: CurrencyListItemRepresentable) {
        listItemCurrencyCodeTv.text = item.code
        listItemCurrencyNameTv.text = item.name

        listItemCurrencyValueEt.run {
            if (item.isSelected) makeEditable(item) else makeReadOnly(item)
        }

        Glide.with(containerView.context)
            .load(item.flagUrl)
            .circleCrop()
            .into(listItemCurrencyFlagIv)

        listItemCurrencyRootContainer.setOnClickListener {
            listener.onItemSelected(
                code = item.code,
                amount = listItemCurrencyValueEt.text.toString().toDouble()
            )
        }
    }

    private fun FxRatesEditText.makeReadOnly(item: CurrencyListItemRepresentable) {
        removeTextChangedListener(textWatcher)
        setOnEditorActionListener(null)
        observeAmountChanges(item.code, amountToDisplayObservable)
        setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listener.onItemSelected(
                    code = item.code,
                    amount = listItemCurrencyValueEt.text.toString().toDouble()
                )
            }
        }
    }

    private fun FxRatesEditText.makeEditable(item: CurrencyListItemRepresentable) {
        onFocusChangeListener = null
        stopObservingAmountChanges()
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener.onAmountChanged(
                    code = item.code,
                    amount = listItemCurrencyValueEt.text.toString().toDouble()
                )
            }
            false
        }
        addTextChangedListener(textWatcher)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            amountToDisplayObservable: Observable<Map<String, Double>>,
            listener: CurrencyListActionsListener
        ): CurrencyListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_currency, parent, false)
            return CurrencyListViewHolder(
                view,
                amountToDisplayObservable,
                listener
            )
        }
    }
}
