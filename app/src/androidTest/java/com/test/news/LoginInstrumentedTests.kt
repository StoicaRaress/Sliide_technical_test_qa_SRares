package com.test.news

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.test.news.features.login.presentation.LoginActivity
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class LoginInstrumentedTests {

    companion object {
        private const val VALID_USER_NAME = "user1"
        private const val INVALID_USER_NAME = "username"
        private const val VALID_USER_PASSWORD = "password"
        private const val INVALID_USER_PASSWORD = "wordpass"
    }

    @get:Rule
    var activityTestRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    //Scenario 1
    @Test
    fun shouldCheckUIElementsOnFirstTimeLogin() {
        onView(withId(R.id.editTextUserName))
            .check(matches(isDisplayed()))
        onView(withId(R.id.editTextPassword))
            .check(matches(isDisplayed()))
        onView(withId(R.id.buttonLogin))
            .check(matches(isDisplayed()))
    }

    //Scenario 2 part 1 - will fail until Bug no.1 is solved
    @Test
    fun shouldLoginWithInvalidUsername() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(INVALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        Thread.sleep(2000)
        onView(withText("Invalid username"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
    }

    //Scenario 2 part 2
    @Test
    fun shouldLoginWithInvalidPassword() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(INVALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        Thread.sleep(2000)
        onView(withText("Wrong password"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
    }

    //Scenario 3
    @Test
    fun shouldLoginWithValidCredentials() {
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        assertTrue(activityTestRule.getActivity().isFinishing())
    }

    //Scenario 4 - will fail until Bug no.2 is solved
    @Test
    fun shouldStayLoggedInAfterExitingTheApp(){
        onView(withId(R.id.editTextUserName))
            .perform(clearText(), typeText(VALID_USER_NAME))
        onView(withId(R.id.editTextPassword))
            .perform(clearText(), typeText(VALID_USER_PASSWORD))
        onView(withId(R.id.buttonLogin))
            .perform(click())
        try {
            Espresso.pressBackUnconditionally()
        } catch (e: Exception){}
        activityTestRule.launchActivity(null)
        assertTrue(activityTestRule.getActivity().isFinishing())
    }
}

