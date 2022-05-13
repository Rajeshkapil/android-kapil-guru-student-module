package com.kapilguru.student.demoLecture.demoLectureDetails.model

import com.google.gson.annotations.SerializedName

data class DemoLectureRegisterStatusResData(
    @SerializedName("lecture_id") val lectureId: Int = 0,
    @SerializedName("attendee_id") val attendeeId: Int = 0,
    @SerializedName("status") val status: String = "")