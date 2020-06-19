package com.kevalpatel2106.fxratesample.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.kevalpatel2106.fxratesample.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class FxRatesEditText(
    context: Context,
    attributes: AttributeSet
) : AppCompatEditText(context, attributes, androidx.appcompat.R.attr.editTextStyle) {

    private val compositeDisposable = CompositeDisposable()

    fun observeAmountChanges(code: String, observable: Observable<Map<String, Double>>) {
        compositeDisposable.clear()

        observable
            .filter { !isFocused }
            .map { it[code] ?: 0.0 }
            .subscribe({ amountToDisplay ->
                setText(amountToDisplay.toStringUpToTwoDecimal())
            }, {
                setText(context.getString(R.string.error_amount))
            })
            .addTo(compositeDisposable)
    }

    fun stopObservingAmountChanges() {
        compositeDisposable.clear()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }
}
