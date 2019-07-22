package com.workday.vizzy.model

import com.google.gson.annotations.SerializedName

/*
    This response is identical to BuildInfo, maybe the should use the same class
 */
/*
{
	"id": 1,
	"dev_build": true,
	"commit_sha": null,
	"pull_request_number": null,
	"url": null,
	"temporary": true,
	"title": "Dev Build",
	"created_at": "2019-04-26T02:38:36.595-07:00",
	"updated_at": "2019-04-26T06:38:47.072-07:00",
	"image_md5s": {
		"light_colors/000001": "0c3ff6359554d40d7023926eaf140498",
		"light_colors/000002": "98810d52b1c7385325477282ddfb4dc0"
	},
	"base_image_count": 0
}
 */
class ImageMd5Response {
    @SerializedName("image_md5s")
    var imageMd5s: Map<String, String>? = null
}
