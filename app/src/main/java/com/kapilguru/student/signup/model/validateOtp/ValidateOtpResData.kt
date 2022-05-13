package com.kapilguru.student.signup.model.validateOtp

data class ValidateOtpResData(
    val contactNumber: String,
    val countryCode: Int,
    val emailId: String,
    val isAdmin: Int,
    val isStudent: Int,
    val isTrainer: Int,
    val isVisitor: Int,
    val name: String,
    val password: String
)