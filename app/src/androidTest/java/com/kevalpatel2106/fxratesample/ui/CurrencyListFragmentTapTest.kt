package com.kevalpatel2106.fxratesample.ui

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class CurrencyListFragmentTapTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun whenTapOnTheFourthRowCheckItMovesToFirstRow() {
        // Tap on the fourth row which has currency code ZAR
        val recyclerView = onView(
            allOf(
                withId(R.id.currencyListRv),
                childAtPosition(withId(R.id.currencyListRootContainer), 1)
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        Thread.sleep(600)

        // Check if it is moved to first tow
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
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("ZAR")))
    }
}
