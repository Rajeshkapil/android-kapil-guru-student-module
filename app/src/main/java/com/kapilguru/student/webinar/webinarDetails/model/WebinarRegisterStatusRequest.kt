package com.kapilguru.student.webinar.webinarDetails.model

import com.google.gson.annotations.SerializedName

data class WebinarRegisterStatusRequest(
    @SerializedName("webinar_id") val webinarId: Int = 0,
    @SerializedName("attendee_id") val attendeeId: String = "")