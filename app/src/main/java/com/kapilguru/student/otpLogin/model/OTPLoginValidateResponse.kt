package com.kapilguru.student.otpLogin.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.otpLogin.model.OTPLoginValidateResData

data class OTPLoginValidateResponse(
    @SerializedName("data") val otpLoginValidateResData : List<OTPLoginValidateResData>? = null,
    @SerializedName("message") val message : String ? = "",
    @SerializedName("status") val status : Int? = -1
)
