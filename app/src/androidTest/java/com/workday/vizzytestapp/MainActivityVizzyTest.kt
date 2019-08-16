package com.workday.vizzytestapp

import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.workday.vizzy.addVizzyTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainActivityVizzyTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test fun sampleVisualTest() {
        activityRule.addVizzyTest()
    }

    @Test fun anotherVisualTest() {
        activityRule.addVizzyTest()
        AccessibilityChecks.accessibilityAssertion()
        activityRule.addVizzyTest("WithName")
        activityRule.addVizzyTest("WithName2")
    }
}
