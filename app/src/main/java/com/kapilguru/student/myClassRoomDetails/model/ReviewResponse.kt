package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("status") var status: Int,
    @SerializedName("message") val message: String
)
