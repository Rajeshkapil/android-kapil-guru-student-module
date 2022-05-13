package com.kapilguru.student.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginValidateRequest(
    @SerializedName("email_phone") var emailOrPhone : String = "",
    @SerializedName("otp_type") var otpType : String? = "",
    @SerializedName("user_role") var userRole : String? = "student",
    @SerializedName("uuid") var uuid : String? = "")