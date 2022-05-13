package com.kapilguru.student.ui.changePassword.model

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("token") var token: String = "")