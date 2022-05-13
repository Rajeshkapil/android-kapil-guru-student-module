package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class TopCategoriesResponse(
    @SerializedName("data") val data: List<TopCategoriesApi>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0
)