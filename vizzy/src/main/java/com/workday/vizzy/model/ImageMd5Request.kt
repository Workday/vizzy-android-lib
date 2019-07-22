package com.workday.vizzy.model

import com.google.gson.annotations.SerializedName

class ImageMd5Request {
    @SerializedName("image_md5s")
    var imageMd5s: String = ""
}
