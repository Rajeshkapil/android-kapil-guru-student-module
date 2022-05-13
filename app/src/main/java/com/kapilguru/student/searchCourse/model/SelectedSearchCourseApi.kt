package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class SelectedSearchCourseApi(
    @SerializedName("nonPositionArray")
    val nonPositionArray: List<PositionArrayItem>?,
    @SerializedName("positionData")
    val positionData: List<PositionDataItem>?,
    @SerializedName("positionArray")
    val positionArray: List<PositionArrayItem>?
)