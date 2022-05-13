package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class ContactTrainerRequest(
    @SerializedName("email_id")
    var emailId: String = "",
    @SerializedName("country_code")
    var countryCode: Int = 0,
    @SerializedName("course_id")
    var courseId: Int = 0,
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("student_id")
    var studentId: Int = 0,
    @SerializedName("uuid")
    var uuid: String = "",
    @SerializedName("contact_number")
    var contactNumber: String = "",
    @SerializedName("trainer_id")
    var trainerId: Int = 0
)