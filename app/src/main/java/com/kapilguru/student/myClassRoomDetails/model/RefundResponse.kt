package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class RefundResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") val message: String
)
