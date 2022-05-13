package com.kapilguru.student.signup.model.validateOtp

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.signup.model.validateOtp.ValidateOtpResData

data class ValidateOtpResponse(
    @SerializedName("data")val validateOtpResData: ValidateOtpResData,
    val message: String,
    val status: Int
)