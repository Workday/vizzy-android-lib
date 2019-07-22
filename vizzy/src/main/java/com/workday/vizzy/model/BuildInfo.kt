package com.workday.vizzy.model

import com.google.gson.annotations.SerializedName

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
    "updated_at": "2019-04-26T02:38:36.595-07:00",
    "image_md5s": null,
    "base_image_count": 0
}
 */

class BuildInfo {
    @SerializedName("id")
    var id: Long = 0
}
