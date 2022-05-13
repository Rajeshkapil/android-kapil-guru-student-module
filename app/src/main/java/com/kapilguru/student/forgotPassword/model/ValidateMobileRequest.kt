package com.kapilguru.student.forgotPassword.model

import com.google.gson.annotations.SerializedName

data class ValidateMobileRequest(
    @SerializedName("contact_number") var contactNo : String = ""
)
