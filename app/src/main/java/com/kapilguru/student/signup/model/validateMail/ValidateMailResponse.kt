package com.kapilguru.student.signup.model.validateMail

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.signup.model.validateMail.ValidateMailResData

data class ValidateMailResponse(
    @SerializedName("data") val validateMailData: ValidateMailResData,
    val message: String,
    val status: Int
)