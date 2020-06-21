package com.kevalpatel2106.fxratesample.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.kevalpatel2106.fxratesample.R
import com.kevalpatel2106.fxratesample.childAtPosition
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CurrencyListFragmentInitializationTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkListExists() {
        val recyclerView = onView(
            allOf(
                withId(R.id.currencyListRv),
                childAtPosition(
                    allOf(
                        withId(R.id.currencyListRootContainer),
                        childAtPosition(withId(R.id.currencyListFragmentContainer), 0)
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
    }

    @Test
    fun checkFirstItemIsBaseCurrencyEur() {
        val textView = onView(
            allOf(
                withId(R.id.listItemCurrencyCodeTv),
                childAtPosition(
                    allOf(
                        withId(R.id.listItemCurrencyRootContainer),
                        childAtPosition(withId(R.id.currencyListRv), 0)
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("EUR")))

        val eurRatesEt = onView(
            allOf(
                withId(R.id.listItemCurrencyValueEt), withText("1.00"),
                childAtPosition(
                    allOf(
                        withId(R.id.listItemCurrencyRootContainer),
                        childAtPosition(withId(R.id.currencyListRv), 0)
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        eurRatesEt.check(matches(withText("1.00")))
    }
}
