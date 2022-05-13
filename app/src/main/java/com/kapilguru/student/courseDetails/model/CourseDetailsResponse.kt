package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class CourseDetailsResponse(
    @SerializedName("allData")
    val allData: AllData,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)