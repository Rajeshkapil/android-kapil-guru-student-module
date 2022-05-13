package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class NotifyNewKeyWordResponse(
    @SerializedName("data")
    var data: NotifyNewKeyWordResponseApi?=null,
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("status")
    var status: Int? = 0
)