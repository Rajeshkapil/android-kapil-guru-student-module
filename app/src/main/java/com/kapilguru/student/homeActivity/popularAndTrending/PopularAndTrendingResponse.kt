package com.kapilguru.student.homeActivity.popularAndTrending

import com.google.gson.annotations.SerializedName

data class PopularAndTrendingResponse(
    @SerializedName("data")
    val popularAndTrendingApi: List<PopularAndTrendingApi>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)