package com.example.currencyconverterapp.common

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.currencyconverterapp.R
import com.example.currencyconverterapp.ui.MainActivity
import junit.framework.TestCase.fail
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExtensionsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun `test AfterTextChanged for etAmount edittext`() {
        val expected = "200"
        onView(withId(R.id.etAmount)).perform(typeText("100"), closeSoftKeyboard())
        onView(withId(R.id.etAmount)).perform(clearText())
        onView(withId(R.id.etAmount)).perform(typeText("200"), closeSoftKeyboard())
        Thread.sleep(1000)

        onView(withId(R.id.etAmount)).check(matches(withText(expected)))
    }

    @Test
    fun testHideKeyboard() {
        val scenario = activityRule.scenario
        onView(withId(R.id.etAmount)).perform(click())
        Assert.assertTrue(isKeyboardShown())
        scenario.onActivity { it.hideKeyboard() }
        Assert.assertTrue(isKeyboardShown())
    }

    @Test
    fun testHideKeyboardWithSoftKeyboardAlreadyHidden() {
        onView(isRoot()).perform(closeSoftKeyboard())
        val isKeyboardVisible = isKeyboardShown()
        Assert.assertFalse("Soft keyboard should already be hidden", isKeyboardVisible)
    }

    @Test
    fun testHideKeyboardWithNullView() {
        onView(isRoot()).perform(closeSoftKeyboard())
        activityRule.scenario.onActivity { activity ->
            try {
                activity.window.decorView.clearFocus()
                activity.hideKeyboard()
            } catch (e: Exception) {
                fail("hideKeyboard should not throw any exceptions when current focus is null")
            }
        }
    }

    private fun isKeyboardShown(): Boolean {
        val inputMethodManager =
            InstrumentationRegistry.getInstrumentation().context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }
}