package com.kapilguru.student.signup.model.validateOtp

data class ValidateOtpRequest(
    var otp: String? = null,
    var uuid: String? = null
)