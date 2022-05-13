package com.kapilguru.student.announcement.sentItems.data

import com.google.gson.annotations.SerializedName

data class SentItemsResponse(
    @SerializedName("data") val data: List<SentItemsData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
