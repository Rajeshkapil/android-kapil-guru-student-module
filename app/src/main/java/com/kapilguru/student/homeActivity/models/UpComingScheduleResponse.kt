package com.kapilguru.student.ui.home

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi

data class UpComingScheduleResponse(
    @SerializedName("data")
    var data: List<UpComingScheduleApi>?,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Int = 0
)