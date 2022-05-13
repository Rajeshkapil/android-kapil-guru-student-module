package com.kapilguru.student.webinar.webinarDetails.model

import com.google.gson.annotations.SerializedName

data class WebinarRegisterStatusResData(
    @SerializedName("webinar_id") val webinarId: Int = 0,
    @SerializedName("attendee_id") val attendeeId: Int = 0,
    @SerializedName("status") val status: String = "") //Registered - value of status if registered