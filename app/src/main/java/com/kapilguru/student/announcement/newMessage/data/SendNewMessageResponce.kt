package com.kapilguru.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName

class SendNewMessageResponce(
    @SerializedName("data") val data: SendNewMessageResponceData? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)