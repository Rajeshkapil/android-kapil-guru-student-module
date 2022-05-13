package com.kapilguru.student.login.models

import com.google.gson.annotations.SerializedName

data class LoginResponseData(
    @SerializedName("isImageUpdated") val isImageUpdated: Int = 0,
    @SerializedName("lastAnnouncementId") val lastAnnouncementId: Int = 0,
    @SerializedName("auth") val auth: Boolean = false,
    @SerializedName("isAdmin") val isAdmin: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("isBankUpdated") val isBankUpdated: Int = 0,
    @SerializedName("token") val token: String = "",
    @SerializedName("isSubscribed") val isSubscribed: Int = 0,
    @SerializedName("user_code") val userCode: String = "",
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("contactNumber") val contactNumber: String = "",
    @SerializedName("isProfileUpdated") val isProfileUpdated: Int = 0,
    @SerializedName("currency") val currency: String = "",
    @SerializedName("isEnrolled") val isEnrolled: Int = 0,
    @SerializedName("isMktg") val isMktg: Int = 0,
    @SerializedName("email") val email: String = "",
    @SerializedName("isStudent") val isStudent: Int = 0,
    @SerializedName("username") val username: String = "",
    @SerializedName("isTrainer") val isTrainer: Int = 0
)