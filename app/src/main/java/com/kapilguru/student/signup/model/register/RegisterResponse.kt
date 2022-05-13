package com.kapilguru.student.signup.model.register

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.signup.model.register.RegisterResData

data class RegisterResponse(
    @SerializedName("data")val registerResData: List<RegisterResData>,
    val message: String,
    val status: Int
)