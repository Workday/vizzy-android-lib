package com.workday.vizzy

import android.app.Activity
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.screenshot.Screenshot
import android.widget.FrameLayout
import java.io.File
import java.io.FileOutputStream

fun <T: Activity> ActivityTestRule<T>.addVizzyTest(screenshotName: String = "") {
    var screenshotTaken = false
    var fileName = screenshotName
    if (fileName.isEmpty()) {
        fileName = Thread.currentThread().stackTrace[4].methodName
    }

    activity.findViewById<FrameLayout>(android.R.id.content).post {
        captureScreenshot(activity, "${activity.localClassName}_${fileName}")
        screenshotTaken = true
    }
    while (!screenshotTaken) {
        Thread.sleep(10)
    }
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
