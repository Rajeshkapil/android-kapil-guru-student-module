package com.kapilguru.student.announcement.newMessage.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.announcement.newMessage.data.NewMessageData

data class NewMessageResponse(
    @SerializedName("data") val data: List<NewMessageData> ? = null,
    @SerializedName("message") val message: String? = "",
    @SerializedName("status") val status: Int? = 0)
