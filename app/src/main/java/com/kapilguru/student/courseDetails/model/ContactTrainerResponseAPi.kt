package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class ContactTrainerResponseAPi(
    @SerializedName("data")
    val contactTrainerResponse: ContactTrainerResponse,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)