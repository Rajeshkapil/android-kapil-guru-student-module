package com.kapilguru.student.signup.model.resendOtp

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("uuid") var uuid: String? = "")