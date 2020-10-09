package com.workday.vizzy

import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import kotlinx.coroutines.runBlocking
import java.io.*


class VizzyxTestRunner : AndroidJUnitRunner() {

    companion object {
        const val VIZZY_DIR = "vizzy"
        private const val VIZZY_USER_EMAIL_KEY = "vizzyUserEmail"
        private const val VIZZY_USER_TOKEN_KEY = "vizzyUserToken"
        private const val VIZZY_BASE_URL_KEY = "vizzyBaseUrl"
        private const val VIZZY_PROJECT_ID_KEY = "vizzyProjectId"

        private const val PULL_REQUEST_NUMBER = "pullRequestNumber"
        private const val COMMIT_SHA = "commitSha"
    }

    lateinit var imagesDir: File
    lateinit var vizzyApiService: VizzyApiService

    private var pullRequestNumber: String? = null
    private var commitSha: String? = null

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        if (null != arguments) {
            // TODO: Verify that the mandatory arguments are being passed
            val vizzyUserEmail = arguments.getString(VIZZY_USER_EMAIL_KEY)!!
            val vizzyUserToken = arguments.getString(VIZZY_USER_TOKEN_KEY)!!
            val vizzyBaseUrl = arguments.getString(VIZZY_BASE_URL_KEY)!!
            val vizzyProjectId = arguments.getString(VIZZY_PROJECT_ID_KEY)!!.toLong()
            vizzyApiService = VizzyApiService(vizzyUserEmail, vizzyUserToken, vizzyBaseUrl, vizzyProjectId)
            // Optional parameters
            pullRequestNumber = arguments.getString(PULL_REQUEST_NUMBER)
            commitSha = arguments.getString(COMMIT_SHA)
        }
    }

    override fun start() {
        // Clean up the vizzy directory
        imagesDir = File("${targetContext.filesDir.path}/$VIZZY_DIR")
        cleanUpVizzy()
        super.start()
    }

    private fun cleanUpVizzy() {
        imagesDir.deleteRecursively()
        imagesDir.mkdirs()
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        // Will return images to upload
        if (resultCode == RESULT_OK) {
            runBlocking {
                vizzyApiService.uploadTestImages(imagesDir, pullRequestNumber, commitSha)
            }
        }
        super.finish(resultCode, results)
    }
}
