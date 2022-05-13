package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class PositionDataItem(
    @SerializedName("course_id")
    var courseId: Int = 0,
    @SerializedName("course_position_id")
    var coursePositionId: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("course_position_num")
    var coursePositionNum: Int = 0
)