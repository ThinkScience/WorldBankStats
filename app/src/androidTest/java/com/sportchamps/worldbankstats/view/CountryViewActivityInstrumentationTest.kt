package com.sportchamps.worldbankstats.view

import android.content.ClipData
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule
import android.util.Log
import com.sportchamps.worldbankstats.R
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.*
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.anything
import org.junit.Before
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import android.content.Intent
import com.sportchamps.worldbankstats.model.data.pojo.Country
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.Espresso.onView




@RunWith(AndroidJUnit4::class)
class CountryViewActivityInstrumentationTest {
    val TAG:String = "@Test-CVAIT"
    @Rule
    @JvmField
    val ruleActivity  = ActivityTestRule(CountryViewActivity::class.java)

    @Before
    fun before_every_function_test(){
        Log.i(TAG," Before every function ...")
    }

    @Test
    fun test_check_spinnerYearSelectText(){
        Espresso.onView(withId(R.id.sortspinnercountries)).perform(click())
        Espresso.onData(anything()).atPosition(1).perform(click())
        Espresso.onView(withId(R.id.sortspinnercountries)).check(matches(withSpinnerText(containsString("Alphabetical Order"))))
    }
}