package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class EnrolledCourseResponse(
    @SerializedName("data")
    var data: List<EnrolledCourseResponseApi>?=null,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
)