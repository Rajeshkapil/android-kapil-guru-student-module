package com.kapilguru.student.forgotPassword.model

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.forgotPassword.model.ValidateMobileResData

data class ValidateMobileResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : ValidateMobileResData
)
