package com.kapilguru.student.exam.model

import com.google.gson.annotations.SerializedName

data class StudentResult(
    @SerializedName("student_name") val studentName: String = "",
    @SerializedName("total_marks") val totalMarks: Int = 0,
    @SerializedName("total_questions") val totalQuestions: Int = 0,
    @SerializedName("unattempted_questions") val unattemptedQuestions: Int = 0,
    @SerializedName("incorrect_answers") val incorrectAnswers: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("correct_answers") val correctAnswers: Int = 0,
    @SerializedName("secured_marks") val securedMarks: Int = 0,
    @SerializedName("total_score") val totalScore: Int = 0,
    @SerializedName("student_code") val studentCode: String = "",
    @SerializedName("time_taken") val timeTaken: Int = 0
)