package com.kapilguru.student.demoLecture.demoLectureDetails.model

import com.google.gson.annotations.SerializedName

class DemoLectureDetailsResponse(
    @SerializedName("data") val demoLectureDataList: ArrayList<DemoLectureDetailsResData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)