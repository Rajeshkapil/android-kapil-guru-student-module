package com.kapilguru.student.signup.model.validateMail

import com.google.gson.annotations.SerializedName

data class ValidateMailRequest(
    @SerializedName("email_id") var emailId: String = ""
)
