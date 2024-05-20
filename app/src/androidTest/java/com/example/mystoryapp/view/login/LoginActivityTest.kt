package com.example.mystoryapp.view.login

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.mystoryapp.R
import com.example.mystoryapp.utils.EspressoIdlingResource
import com.example.mystoryapp.view.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginSuccessAndLogoutSuccess() {
        // Set up the input
        onView(withId(R.id.edLoginEmail)).perform(
            typeText("usermystory@gmail.com"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.edLoginPassword)).perform(
            typeText("mystoryuser123"),
            closeSoftKeyboard()
        )

        // Check when login button is click
        onView(withId(R.id.btnLogin)).perform(click())

        // Check if the progress bar is displayed
        onView(withId(R.id.progressBarLogin)).check(matches(isDisplayed()))

        // Check if the dialog is displayed
        onView(withText(R.string.login_title_alert)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())

        // Check if user success go to main activity
        intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rvListStory)).check(matches(isDisplayed()))

        // Open overflow menu and click logout menu item
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText(R.string.logout)).perform(click())

        // Check if the dialog is displayed
        onView(withText(R.string.logout_title)).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())

        // Check if user success go to login activity
        intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.tvLoginTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun loginFailed() {

        // Set up the input
        onView(withId(R.id.edLoginEmail)).perform(typeText("user@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.edLoginPassword)).perform(
            typeText("mystoryuser123"),
            closeSoftKeyboard()
        )

        // Check when login button is click
        onView(withId(R.id.btnLogin)).perform(click())

        // Check if the progress bar is displayed
        onView(withId(R.id.progressBarLogin)).check(matches(isDisplayed()))

        // Check if the dialog is displayed
        onView(withText(R.string.login_title_alert)).inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())

        activity.scenario.onActivity { activity ->
            assert(activity.isFinishing)  // Check if the activity is finishing
        }
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

}