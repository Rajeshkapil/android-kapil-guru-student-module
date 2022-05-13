package com.kapilguru.student.signup.model.sendOtpSms


import com.google.gson.annotations.SerializedName

data class SendOtpSmsRequest(
    @SerializedName("accepted") var accepted: Boolean? = true,
    @SerializedName("contact_number") var contactNumber: String? = "",
    @SerializedName("country_code") var countryCode: Int? = 91,
    @SerializedName("email_id") var emailId: String? = "",
    @SerializedName("is_student") var isStudent: Boolean = true,
    @SerializedName("is_trainer") var isTrainer: Boolean = false,
    @SerializedName("name") var name: String? = "",
    @SerializedName("password") var password: String? = "",
    @SerializedName("uuid") var uuid: String? = ""
)