package com.kapilguru.student.demoLecture.demoLectureDetails.model

import com.google.gson.annotations.SerializedName

data class DemoLectureRegisterResponse(
    @SerializedName("data") val demoLectureRegisterResData: DemoLectureRegisterResData,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)