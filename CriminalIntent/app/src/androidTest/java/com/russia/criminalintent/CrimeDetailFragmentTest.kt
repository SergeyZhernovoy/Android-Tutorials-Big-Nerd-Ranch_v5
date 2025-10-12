package com.russia.criminalintent


import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test


class CrimeDetailFragmentTest {

 private lateinit var scenario: FragmentScenario<CrimeDetailFragment>

@Before
 fun setUp() {
   scenario = FragmentScenario.launchInContainer(CrimeDetailFragment::class.java)
 }

@After
 fun tearDown() {
   scenario.close()
 }

    @Test
    fun crimeTitle_isUpdated_whenTextChanged() {
        // Launch the CrimeDetailFragment
        val scenario = launchFragmentInContainer<CrimeDetailFragment>()

        // Type text into the title field and check if the text is reflected
        val newTitle = "New Crime Title"
        onView(withId(R.id.crime_title))
            .perform(clearText(), typeText(newTitle))

        // Verify if the title field has the new title text
        onView(withId(R.id.crime_title)).check(matches(withText(newTitle)))
    }

    @Test
    fun crimeDate_isDisplayedCorrectly() {
        // Launch the CrimeDetailFragment
        val scenario = launchFragmentInContainer<CrimeDetailFragment>()

        // Check if the crime date is displayed (assuming you format it in a specific way)
        onView(withId(R.id.crime_date))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled()))) // Ensure it's disabled as intended
    }

    @Test
    fun crimeSolved_isUpdated_whenChecked() {
        // Launch the CrimeDetailFragment
        val scenario = launchFragmentInContainer<CrimeDetailFragment>()

        // Check the "crimeSolved" checkbox and verify if we can interact with it
        onView(withId(R.id.crime_solved))
            .perform(click()) // Click to check it
            .check(matches(isChecked())) // Ensure it is checked

        // Click again to uncheck
        onView(withId(R.id.crime_solved))
            .perform(click()) // Click to uncheck
            .check(matches(not(isChecked()))) // Ensure it is unchecked
    }


}