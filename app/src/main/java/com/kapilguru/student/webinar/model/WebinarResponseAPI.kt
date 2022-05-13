package com.kapilguru.student.webinar.model

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class WebinarResponseAPI(
    @SerializedName("allData")
    val webinarResponse: WebinarResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)