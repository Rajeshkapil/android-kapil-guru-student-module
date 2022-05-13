package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class AutoSearchModelResponse(
    @SerializedName("data")
    val autoSearchModelApi: List<AutoSearchModelApi>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)