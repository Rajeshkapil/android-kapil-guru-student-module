package com.kapilguru.student.webinar.model

import com.google.gson.annotations.SerializedName

data class WebinarResponse(
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("ongoing")
    val ongoing: String = "",
    @SerializedName("active")
    val active: String = ""
)