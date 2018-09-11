package com.sportchamps.worldbankstats.view

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
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
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.anything
import org.junit.Before
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.RootMatchers.withDecorView

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {
    var appPackageName:String = "com.sportchamps.worldbankstats"
    val TAG:String = "@Test-MAIT"

    @Before
    fun before_every_function_test(){
        Log.i(TAG,"Hello ")
    }

    @Rule
    @JvmField
    public val rule  = ActivityTestRule(MainActivity::class.java)
    @Rule
    @JvmField
    val rule2  = ActivityTestRule(CountryViewActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals(appPackageName, appContext.packageName)
    }

    @Test
    fun test_check_spinnerYearSelect_updatedText(){
        Log.i(TAG,"Performing app_launch_check_spinnerText test")
        Espresso.onView(withId(R.id.yearSelect)).perform(click())
        Espresso.onData(anything()).atPosition(2).perform(click())
        Espresso.onView(withId(R.id.yearSelect)).check(matches(withSpinnerText(containsString("2015"))))
    }


    @Test
    fun test_check_spinnerSortSelectText_updatedText(){
        Log.i(TAG,"Performing app_launch_check_spinnerText test")
        Espresso.onView(withId(R.id.sortView)).perform(click())
        Espresso.onData(anything()).atPosition(1).perform(click())
        Espresso.onView(withId(R.id.sortView)).check(matches(withSpinnerText(containsString("Alphabetical Order"))))
    }

    @Test
    fun test_check_recyclerview_itemClick_defaultSortGdp(){
        onView(withId(R.id.rv_worlddata))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MyRegionViewAdapter.ViewHolder>(2, click()))
          Espresso.onView(withId(R.id.sortspinnercountries)).check(matches(withSpinnerText(containsString("GDP"))))
    }

    @Test
    fun test_check_recyclerViewClick_toastMessageCheck(){
        val activity = rule2.activity
        val expectedToastMessage = " Item : South Sudan view got clicked"
        onView(withId(R.id.rv_worlddata))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MyRegionViewAdapter.ViewHolder>(0, click()))
        Espresso.onView(withId(R.id.sortspinnercountries)).check(matches(withSpinnerText(containsString("GDP"))))
        Espresso.onView(withId(R.id.rv_countrydata))
                .perform(RecyclerViewActions.actionOnItemAtPosition<MyCountryViewAdapter.ViewHolder>(1, click()))

        onView(withText(expectedToastMessage)).inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
    }
}