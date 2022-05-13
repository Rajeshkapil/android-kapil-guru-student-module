package com.kapilguru.student.ui.profile.data

import com.google.gson.annotations.SerializedName

data class GetOTPResponce(
    @SerializedName("status") var status : String? = null,
    @SerializedName("message") var message : String ? = null
)