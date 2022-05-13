package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class RaiseComplaintResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") val message: String,
)

