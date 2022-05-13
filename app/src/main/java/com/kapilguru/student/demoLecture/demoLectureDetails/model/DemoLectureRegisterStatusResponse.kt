package com.kapilguru.student.demoLecture.demoLectureDetails.model

import com.google.gson.annotations.SerializedName

data class DemoLectureRegisterStatusResponse(
    @SerializedName("data") val demoLectureRegisterStatusResData: List<DemoLectureRegisterStatusResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)