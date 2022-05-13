package com.kapilguru.student.demoLecture.demoLectureDetails.model

import com.google.gson.annotations.SerializedName

data class DemoLectureRegisterRequest(
    @SerializedName("lecture_id") val lectureId: Int = 0,
    @SerializedName("user_id") val trainerId: Int = 0,
    @SerializedName("attendee_id") val attendeeId: String = "",
    @SerializedName("status") val status: String = "Registered") //Registered