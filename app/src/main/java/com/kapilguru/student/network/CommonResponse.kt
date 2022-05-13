package com.kapilguru.student.network

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("data") val logoutResData: CommonResponseData,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)