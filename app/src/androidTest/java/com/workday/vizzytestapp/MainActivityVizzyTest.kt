package com.workday.vizzytestapp

import android.app.Activity
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.workday.vizzy.addVizzyTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainActivityVizzyTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java) as ActivityTestRule<Activity>

    @Test fun sampleVisualTest() {
        activityRule.addVizzyTest()
    }

    @Test fun anotherVisualTest() {
        activityRule.addVizzyTest()
        activityRule.addVizzyTest("WithName")
        activityRule.addVizzyTest("WithName2")
    }
}
