package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class SelectedSearchCourseResponse(
    @SerializedName("data")
    val selectedSearchCourseApi: SelectedSearchCourseApi?=null,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("status")
    val status: Int? = 0
)