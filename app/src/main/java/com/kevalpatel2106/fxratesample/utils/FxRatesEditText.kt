package com.kevalpatel2106.fxratesample.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.entity.Amount
import com.kevalpatel2106.fxratesample.entity.CurrencyCode
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FxRatesEditText(
    context: Context,
    attributes: AttributeSet
) : AppCompatEditText(context, attributes, androidx.appcompat.R.attr.editTextStyle) {

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (isInitialValueSet && isEditable) amountChangeListener?.onAmountChanged(getAmount())
        }
    }
    private val compositeDisposable = CompositeDisposable()

    @Volatile
    private var isInitialValueSet = false
    private var isEditable = false
    private var amountChangeListener: AmountInputChangedListener? = null

    fun setUpReadOnly(
        code: String,
        displayAmountObservable: Observable<Map<CurrencyCode, Amount>>
    ) {
        reset()
        isEditable = false
        observeAmountChanges(code, displayAmountObservable)
    }

    fun setUpEditable(
        code: String,
        listener: AmountInputChangedListener,
        displayAmountObservable: Observable<Map<CurrencyCode, Amount>>
    ) {
        reset()
        isEditable = true
        amountChangeListener = listener
        addTextChangedListener(textWatcher)
        observeAmountChanges(code, displayAmountObservable)
    }

    fun getAmount(): Amount {
        return if (text.isNullOrBlank()) emptyAmount() else text.toString().toDouble()
    }

    private fun setAmount(amount: Amount) {
        setText(amount.toStringUpToTwoDecimal())
        setSelection(text?.length ?: 0)
    }

    private fun observeAmountChanges(
        code: CurrencyCode,
        displayAmountObservable: Observable<Map<CurrencyCode, Amount>>
    ) {
        displayAmountObservable
            .doOnSubscribe { isInitialValueSet = false }
            .filter { !isEditable || !isInitialValueSet }
            .map { it[code] ?: emptyAmount() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ amountToDisplay ->
                setAmount(amountToDisplay)
                isInitialValueSet = true
            }, {
                it.printStackTrace()
                setText(context.getString(R.string.error_amount))
            })
            .addTo(compositeDisposable)
    }

    private fun reset() {
        compositeDisposable.clear()
        removeTextChangedListener(textWatcher)
        setOnEditorActionListener(null)
        isInitialValueSet = false
        isEditable = false
        onFocusChangeListener = null
        amountChangeListener = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    interface AmountInputChangedListener {
        fun onAmountChanged(newAmount: Double)
    }
}
