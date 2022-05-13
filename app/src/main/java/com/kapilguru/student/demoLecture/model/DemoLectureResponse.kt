package com.kapilguru.student.demoLecture.model

import com.google.gson.annotations.SerializedName

data class DemoLectureResponse(
    @SerializedName("summary") val summary: String = "",
    @SerializedName("ongoing") val ongoing: String = "",
    @SerializedName("active") val active: String = ""
)