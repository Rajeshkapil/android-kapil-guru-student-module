package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class SelectedSearchCourseRequest(
    @SerializedName("title")
    val title: String = ""
)