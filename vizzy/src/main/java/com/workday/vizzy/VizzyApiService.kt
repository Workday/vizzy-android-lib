package com.workday.vizzy

import com.google.gson.GsonBuilder
import com.workday.vizzy.Md5Util.calculateMD5
import com.workday.vizzy.model.BuildInfo
import com.workday.vizzy.model.BuildState
import com.workday.vizzy.model.CreateBuildRequest
import com.workday.vizzy.model.ImageMd5Request
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody

class VizzyApiService (val userEmail: String, val userToken: String, val baseUrl: String, val projectId: Long) {

    private val vizzyApi: VizzyApi

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        vizzyApi = retrofit.create(VizzyApi::class.java)
    }

    fun uploadTestImages(imagesDir: File, pullRequestNumber: String?, commitSha: String?): Boolean {
        // This is a long background operation
        // First we create a new build
        val buildInfo = createNewBuild(projectId, pullRequestNumber, commitSha)
        val imagesToUpload = filterImagesToUpload(imagesDir, buildInfo!!.id)
        // Upload them all (potentially in parallel)
        for (fileName in imagesToUpload) {
            // For each file, we upload it
            uploadImage(buildInfo.id, imagesDir, fileName)
        }
        // Read the infor until images are processed
//        do {
            val buildState = getBuildState(buildInfo.id)
//        }
//        while (buildState.)
        // Commit the build to mark it as completed
        return commitBuild(buildInfo.id)
    }

    private fun commitBuild(buildId: Long): Boolean {
        val commitBuildResponse = vizzyApi.commitBuild(userEmail, userToken, buildId).execute()
        return commitBuildResponse.isSuccessful
    }

    private fun filterImagesToUpload(imagesDir: File, buildId: Long): List<String> {
        // We calculate the md5 of all the images
        val imageMd5body = calculateMd5s(imagesDir)
        val imageMd5request = vizzyApi.postImageMd5s("application/json",
            userEmail,
            userToken,
            buildId,
            imageMd5body)
        // Submit the MD5 to the server, will reply which of them should be uploaded
        val imagesToUploadResponse = imageMd5request.execute().body()
        return imagesToUploadResponse?.imageMd5s!!.keys.toList()
    }

    private fun getBuildState(buildId: Long): BuildState? {
        val buildRequest = CreateBuildRequest()
        buildRequest.projectId = projectId
        val request = vizzyApi.getBuildState(userEmail, userToken, buildId)

        // We don't need to have the build info right now, we can put it on the background and start executing tests
        val response = request.execute()
        if (response.isSuccessful) {
            // We can access the build number
            return response.body()
        }
        return null
    }

    private fun createNewBuild(aProjectId: Long, aPullRequestNumber: String?, aCommitSha: String?): BuildInfo? {
        val buildRequest = CreateBuildRequest().apply {
            projectId = aProjectId
            if (aPullRequestNumber != null) {
                pullRequestNumber = aPullRequestNumber
            }
            else {
                devBuild = true
            }
            if (aCommitSha != null) {
                commitSha = aCommitSha
            }
        }

        val request = vizzyApi.createBuild("application/json", userEmail, userToken, buildRequest)

        // We don't need to have the build info right now, we can put it on the background and start executing tests
        val response = request.execute()
        if (response.isSuccessful) {
            // We can access the build number
            return response.body()
        }
        return null
    }

    private fun uploadImage(buildId: Long, imagesDir: File, fileName: String) {
        val file = File(imagesDir, fileName)
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            RequestBody.create(MediaType.parse("image/png"), file)
        )
        val buildIdPart = MultipartBody.Part.createFormData("build_id", buildId.toString())
        val ancestryPart = MultipartBody.Part.createFormData("test_image_ancestry", fileName)

        vizzyApi.uploadImages(userEmail, userToken, imagePart, buildIdPart, ancestryPart).execute()
    }

    private fun calculateMd5s(imagesDir: File): ImageMd5Request {
        val allFiles = getAllFilesUnderData(imagesDir)
        val imagesMap = mutableMapOf<String, String>()
        for (file in allFiles) {
             val md5 = calculateMD5(file)
             // We get the path from the app directory, remove that part
             val imagePath = file.absolutePath.replace("${imagesDir.absolutePath}/", "")
             imagesMap[imagePath] = md5
        }
        val imageMd5Request = ImageMd5Request()
        val gson = GsonBuilder().setPrettyPrinting().create()
        imageMd5Request.imageMd5s = gson.toJson(imagesMap)
        return imageMd5Request
    }

    private fun getAllFilesUnderData(imagesDir: File): List<File> {
        val allImages = mutableListOf<File>()
        for (file in imagesDir.listFiles()) {
            if (file.isDirectory) {
                allImages.addAll(getAllFilesUnderData(file))
            }
            else {
                allImages.add(file)
            }
        }
        return allImages
    }
}
