package com.kapilguru.student.courseDetails.review.model

import com.google.gson.annotations.SerializedName

data class StudentReviewResponse(

    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    var data: List<StudentReviewedData>
)

data class StudentReviewedData(
    @SerializedName("student_name")
    val studentName: String,
    @SerializedName("student_rating")
    var studentRating: Double,
    @SerializedName("student_rating_text")
    val studentRatingText: String,
    @SerializedName("ratings_date")
    val ratingsDate: String
)
