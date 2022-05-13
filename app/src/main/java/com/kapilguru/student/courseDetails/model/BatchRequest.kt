package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class BatchRequest(

    @SerializedName("course_id")
    var courseId: Int,
    @SerializedName("requestor_id")
    var requestorId: Int,
    @SerializedName("description")
    val description: String
)
