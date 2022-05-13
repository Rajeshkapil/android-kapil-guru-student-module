package com.kapilguru.student.signup.model.register

import com.google.gson.annotations.SerializedName

data class RegisterResData(
    val accepted: Int,
    @SerializedName("contact_number") val contactNumber: String,
    @SerializedName("country_code") val countryCode: Int,
    @SerializedName("created_by") val createdBy: Int,
    @SerializedName("created_date") val createdDate: String,
    @SerializedName("email_id")val emailId: String,
    val id: Int,
    @SerializedName("is_active")val isActive: Int,
    @SerializedName("is_admin")val isAdmin: Int,
    @SerializedName("is_female")val isFemale: Int,
    @SerializedName("is_male")val isMale: Int,
    @SerializedName("is_mktg")val isMktg: Int,
    @SerializedName("is_student")val isStudent: Int,
    @SerializedName("is_trainer")val isTrainer: Int,
    @SerializedName("is_visitor")val isVisitor: Int,
    @SerializedName("modified_by")val modifiedBy: Int,
    @SerializedName("modified_date")val modifiedDate: Any,
    val name: String,
    val otp: String,
    val password: String,
    val uuid: String
)