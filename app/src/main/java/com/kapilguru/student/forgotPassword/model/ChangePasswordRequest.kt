package com.kapilguru.student.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("contact_number") var contactNo: String? = "",
    @SerializedName("password") var password: String? = ""
)
