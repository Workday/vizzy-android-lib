package com.workday.vizzy

import com.workday.vizzy.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface VizzyApi {

    @POST("builds.json")
    fun createBuild(@Header("Content-Type") contentType: String,
                    @Header("X-User-Email") email: String,
                    @Header("X-User-Token") token: String,
                    @Body build: CreateBuildRequest
    ) : Call<BuildInfo>

    @Multipart
    @POST("test_images.json")
    fun uploadImages(@Header("X-User-Email") email: String,
                     @Header("X-User-Token") token: String,
                     @Part filePart: MultipartBody.Part,
                     @Part buildIdPart: MultipartBody.Part,
                     @Part ancestorPart: MultipartBody.Part
    ): Call<UploadImageResponse>

    @POST("builds/{id}/add_md5s.json")
    fun postImageMd5s(@Header("Content-Type") contentType: String,
                      @Header("X-User-Email") email: String,
                      @Header("X-User-Token") token: String,
                      @Path("id") buildId: Long,
                      @Body imageRequest: ImageMd5Request
    ) : Call<ImageMd5Response>

    @POST("builds/{id}/commit.json")
    fun commitBuild(@Header("X-User-Email") email: String,
                    @Header("X-User-Token") token: String,
                    @Path("id") buildId: Long
    ) : Call<BuildState>

    @GET("builds/{id}/commit.json")
    fun getBuildState(@Header("X-User-Email") email: String,
                      @Header("X-User-Token") token: String,
                      @Path("id") buildId: Long
    ) : Call<BuildState>
}
