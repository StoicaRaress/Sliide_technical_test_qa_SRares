package com.test.news

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.model.Atoms.getCurrentUrl
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.rule.ActivityTestRule
import com.test.news.features.login.presentation.LoginActivity
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test


class NewsInstrumentedTest : NewsTestBase() {

    companion object {
        private const val VALID_USER_NAME = "user1"
        private const val VALID_USER_PASSWORD = "password"
    }

    @get:Rule
    var activityTestRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    //Scenario 1
    @Test
    fun shouldAfterLoginImagesAreLoaded() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())

        Thread.sleep(2000)
        onView(allOf(withId(R.id.recyclerViewNews), hasMinimumChildCount(3)))
            .check(matches(isDisplayed()))
    }

    //Scenario 2
    @Test
    fun shouldLoginWithNoInternetConnection() {
       /**I was not able to find a working method for turning off mobile data and wifi in order to properly run this test**/
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.textViewError))
            .check(matches(isDisplayed()))
    }

    //Scenario 3
    @Test
    fun shouldOpenBrowserWhenImageClicked() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(NewsInstrumentedTest.VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        Thread.sleep(2000)
        onView(withIndex(withId(R.id.imageView),1))
            .perform(click())
        onWebView().check(webMatches(getCurrentUrl(), containsString("https")))
        /**Tried to find a way for Espresso to check the UI of the browser but another tool is needed**/
    }
}
