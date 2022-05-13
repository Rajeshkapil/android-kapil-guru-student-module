package com.kapilguru.student.ui.profile.data

import com.google.gson.annotations.SerializedName

data class CheckOTPResponce(
    @SerializedName("status") var status : Int,
    @SerializedName("message") var message : String = ""
)
