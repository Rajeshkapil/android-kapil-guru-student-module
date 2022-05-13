package com.kapilguru.student.otpLogin.model

import com.google.gson.annotations.SerializedName

data class OTPLoginValidateResData(
    @SerializedName("result") var result: Int? = -1,
)
