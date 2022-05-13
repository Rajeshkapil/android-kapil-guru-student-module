package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class AllDemosResponse(
    @SerializedName("data") val data: List<AllDemosApi>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)