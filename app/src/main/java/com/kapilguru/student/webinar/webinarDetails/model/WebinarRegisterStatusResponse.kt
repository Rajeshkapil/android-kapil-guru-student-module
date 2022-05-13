package com.kapilguru.student.webinar.webinarDetails.model

import com.google.gson.annotations.SerializedName

data class WebinarRegisterStatusResponse(
    @SerializedName("data") val data: List<WebinarRegisterStatusResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)