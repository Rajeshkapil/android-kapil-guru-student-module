package com.kapilguru.student.signup.model.sendOtpSms


import com.google.gson.annotations.SerializedName

data class SendOptSmsResponseApi(
    @SerializedName("accepted") var accepted: Int?,
    @SerializedName("contact_number") var contactNumber: String?,
    @SerializedName("country_code") var countryCode: Int?,
    @SerializedName("created_by") var createdBy: Int?,
    @SerializedName("created_date") var createdDate: String?,
    @SerializedName("email_id") var emailId: String?,
    @SerializedName("id") var id: Int?,
    @SerializedName("is_active") var isActive: Int?,
    @SerializedName("is_admin") var isAdmin: Int?,
    @SerializedName("is_female") var isFemale: Int?,
    @SerializedName("is_male") var isMale: Int?,
    @SerializedName("is_mktg") var isMktg: Int?,
    @SerializedName("is_sent") var isSent: Int?,
    @SerializedName("is_student") var isStudent: Int?,
    @SerializedName("is_trainer") var isTrainer: Int?,
    @SerializedName("is_visitor") var isVisitor: Int?,
    @SerializedName("modified_by") var modifiedBy: Int?,
    @SerializedName("modified_date") var modifiedDate: Any?,
    @SerializedName("name") var name: String?,
    @SerializedName("otp") var otp: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("uuid") var uuid: String?
)