package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class NotificationCountResponse(
    @SerializedName("data") var notificationCountResponseApi: List<DataItem>?=null, @SerializedName("message") var message: String = "", @SerializedName("status") var status: Int = 0
)