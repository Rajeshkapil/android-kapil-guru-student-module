package com.kapilguru.student.demoLecture.model

import com.google.gson.annotations.SerializedName

data class DemoLectureResponseAPI(
    @SerializedName("allData") val demoLectureResponse: DemoLectureResponse,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)