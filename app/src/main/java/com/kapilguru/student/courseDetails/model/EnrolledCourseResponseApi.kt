package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class EnrolledCourseResponseApi(
    @SerializedName("course_id")
    val courseId: Int = 0,
    @SerializedName("batch_id")
    val batchId: Int = 0,
    @SerializedName("student_id")
    val studentId: Int = 0
)