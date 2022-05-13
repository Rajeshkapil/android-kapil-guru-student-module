package com.kapilguru.student.exam.model

import com.google.gson.annotations.SerializedName

data class Responses(
    @SerializedName("course_id") val courseId: Int = 0,
    @SerializedName("question_paper_id") val questionPaperId: Int = 0,
    @SerializedName("correct_opt") val correctOpt: String = "",
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("student_id") val studentId: Int = 0,
    @SerializedName("question_id") val questionId: Int = 0,
    @SerializedName("selected_opt") val selectedOpt: String = "",
    @SerializedName("trainer_id") val trainerId: Int = 0,
    @SerializedName("exam_id") val examId: Int = 0)