package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.courseDetails.model.BatchesItem
import com.kapilguru.student.courseDetails.model.Course

data class AllData(
    @SerializedName("batches")
    val batches: List<BatchesItem>?,
    @SerializedName("showbatch")
    val showbatch: String = "",
    @SerializedName("course")
    val course: Course
)