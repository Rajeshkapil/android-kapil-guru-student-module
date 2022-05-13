package com.kapilguru.student.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginRequest(
    @SerializedName("otp_value") var otpValue: String = "",
    @SerializedName("device") val device: String = "ANDROID",
    @SerializedName("uuid") var uuid: String = "")