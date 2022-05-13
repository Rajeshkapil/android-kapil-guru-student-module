package com.kapilguru.student.login.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LoginResponse(
        @SerializedName("data") val data: LoginResponseData?,
        @SerializedName("message") val message: String,
        @SerializedName("status") val status: Int
)