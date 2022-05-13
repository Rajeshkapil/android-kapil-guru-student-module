package com.kapilguru.student.webinar.webinarDetails.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.login.models.LoginResponseData

data class WebinarDetailsResponse(
    @SerializedName("data") val webinarDetailsDataList : ArrayList<WebinarDetailsResData>? = null ,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
