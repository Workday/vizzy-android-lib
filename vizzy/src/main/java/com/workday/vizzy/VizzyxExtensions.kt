package com.workday.vizzy

import android.app.Activity
import android.widget.FrameLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import java.io.FileOutputStream

fun <A : Activity> ActivityScenario<A>.addVizzyTest(screenshotName: String = "") {
    var screenshotTaken = false
    var fileName = screenshotName
    if (fileName.isEmpty()) {
        fileName = Thread.currentThread().stackTrace[4].methodName
    }

    this.onActivity {
        val contentView = it.findViewById<FrameLayout>(android.R.id.content)
        captureScreenshot(it, "${it.localClassName}_${fileName}")
        screenshotTaken = true
    }
}

private fun captureScreenshot(activity: Activity, fileName: String) {
    val screenCapture = Screenshot.capture(activity)
    screenCapture.name = fileName

    // If we have more than one asset per method, we just append a number
    val vizzyDir = "${activity.filesDir.path}/${VizzyxTestRunner.VIZZY_DIR}"
    val directory = activity.packageName.replace('.', '/')
    val fullDirectory = File("$vizzyDir/$directory")
    fullDirectory.mkdirs()

    val file = File(fullDirectory, "${screenCapture.name}.${screenCapture.format}")
    val fOut = FileOutputStream(file)
    screenCapture.bitmap.compress(screenCapture.format, 85, fOut)
    fOut.flush()
    fOut.close()
}
