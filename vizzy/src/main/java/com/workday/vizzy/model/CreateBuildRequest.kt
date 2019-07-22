package com.workday.vizzy.model

import com.google.gson.annotations.SerializedName

/*
{
	"project_id": 1,
	"dev_build": true
}
 */
class CreateBuildRequest {

    @SerializedName("project_id")
    var projectId: Long = 1

    @SerializedName("dev_build")
    var devBuild = false

    // A pull_request_number of -1 means a master build
    // A pull_request_number of something else will make a PR build
    // A dev build will not require PR number or commit sha
    @SerializedName("pull_request_number")
    var pullRequestNumber = ""

    @SerializedName("commit_sha")
    var commitSha = ""
}
