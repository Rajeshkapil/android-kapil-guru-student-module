package com.kapilguru.student.announcement.inbox.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.announcement.inbox.data.InboxItem

class InboxResponse(
    @SerializedName("data") val data: List<InboxItem> ? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)