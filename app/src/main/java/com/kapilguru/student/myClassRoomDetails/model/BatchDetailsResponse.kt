package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class BatchDetailsResponse(
    @SerializedName("data") var batchDetailsData: List<BatchDetailsData>? = null,
    @SerializedName("message") var message: String? = "",
    @SerializedName("status") var status: Int? = 0
)
