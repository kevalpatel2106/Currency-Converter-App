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

/***
 * Subclass of [AppCompatEditText] that handles showing the currency amount and make it editable.
 *
 * This class can be operated in two modes:
 * 1. [setUpReadOnly] will make this edit text read only and it will observe the changes emitted by
 * displayAmountObservable
 * 2. [setUpEditable] will make this edit text editable and it will stop observing changes emitted by
 * displayAmountObservable as soon as initial value is set. i.e. [isInitialValueSet] is true
 *
 * @see [setUpReadOnly]
 * @see [setUpEditable]
 */
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

    /**
     * Set this edit text as read only. In read only mode, this view
     * - won't observe text changes
     * - change the amount displayed based on [displayAmountObservable]
     *
     * @property code Code of the currency to observe
     * @property displayAmountObservable [Observable] that emits [Map] of currency code and their
     * amount to be displayed
     */
    fun setUpReadOnly(
        code: String,
        displayAmountObservable: Observable<Map<CurrencyCode, Amount>>
    ) {
        reset()
        isEditable = false
        observeAmountChanges(code, displayAmountObservable)
    }

    /**
     * Set this edit text as editable. In editable mode, this view
     * - will observe text changes
     * - won't change the amount displayed based on [displayAmountObservable] after setting the
     * initial value
     *
     * @property code Code of the currency to observe
     * @property displayAmountObservable [Observable] that emits [Map] of currency code and their
     * amount to be displayed
     * @property listener [AmountInputChangedListener] that will notify when amount is edited.
     * (see [onTextChanged])
     */
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

    /**
     * Get current amount displayed in edit text
     *
     * @return [emptyAmount] if text is null or blank else [Amount] displayed
     */
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

    /**
     * Listener to notify subscriber when amount in [FxRatesEditText] is changed
     */
    interface AmountInputChangedListener {

        /**
         * This method will be called when amount in [FxRatesEditText] is changed
         *
         * @property newAmount new [Amount] that is changed
         */
        fun onAmountChanged(newAmount: Amount)
    }
}
