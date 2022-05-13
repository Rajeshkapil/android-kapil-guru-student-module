package com.kapilguru.student.ui.profileInfo.models

import com.google.gson.annotations.SerializedName

data class UploadImageCourseResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)