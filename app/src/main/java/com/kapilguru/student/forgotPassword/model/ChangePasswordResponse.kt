package com.kapilguru.student.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String)

