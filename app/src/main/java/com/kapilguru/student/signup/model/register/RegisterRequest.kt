package com.kapilguru.student.signup.model.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("accepted") var accepted: Boolean = true,
    @SerializedName("contact_number") var contactNumber: String = "",
    @SerializedName("country_code")var countryCode: String = "91", //country code id ex : 91 fo india
    @SerializedName("email_id")var emailId: String = "",
    @SerializedName("is_student")var isStudent: Boolean = true,
    @SerializedName("is_trainer")var isTrainer: Boolean = false,
    @SerializedName("name")var name: String = "",
    @SerializedName("password")var password: String = "",
    @SerializedName("uuid")var uuid: String = ""
)