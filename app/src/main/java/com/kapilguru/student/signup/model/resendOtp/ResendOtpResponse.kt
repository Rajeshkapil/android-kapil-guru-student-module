package com.kapilguru.student.signup.model.resendOtp

import com.google.gson.annotations.SerializedName

data class ResendOtpResponse(
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)