package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class AllWebinarsResponse(
    @SerializedName("data")
    val data: List<AllWebinarsApi>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)