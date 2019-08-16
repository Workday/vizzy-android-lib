package com.workday.vizzy

import android.app.Activity
import android.widget.FrameLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import java.io.FileOutputStream

fun <T: Activity> ActivityTestRule<T>.addVizzyTest(screenshotName: String = "") {
    var screenshotTaken = false
    var fileName = screenshotName
    if (fileName.isEmpty()) {
        fileName = Thread.currentThread().stackTrace[4].methodName
    }

    val contentView = activity.findViewById<FrameLayout>(android.R.id.content)
    contentView.post {
        captureScreenshot(activity, "${activity.localClassName}_${fileName}")
        screenshotTaken = true
    }
    while (!screenshotTaken) {
        Thread.sleep(10)
    }
    AccessibilityChecks.accessibilityAssertion()
}

fun captureScreenshot(activity: Activity, fileName: String) {
    val screenCapture = Screenshot.capture(activity)
    screenCapture.name = fileName

    // If we have more than one asset per method, we just append a number
    val vizzyDir = "${activity.filesDir.path}/${VizzyTestRunner.VIZZY_DIR}"
    val directory = activity.packageName.replace('.', '/')
    val fullDirectory = File("$vizzyDir/$directory")
    fullDirectory.mkdirs()

    val file = File(fullDirectory, "${screenCapture.name}.${screenCapture.format}")
    val fOut = FileOutputStream(file)
    screenCapture.bitmap.compress(screenCapture.format, 85, fOut)
    fOut.flush()
    fOut.close()
}
