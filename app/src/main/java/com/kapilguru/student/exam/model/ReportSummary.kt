package com.kapilguru.student.exam.model

import com.google.gson.annotations.SerializedName

data class ReportSummary(
    @SerializedName("course_id") val courseId: String = "",
    @SerializedName("question_paper_id") val questionPaperId: String = "",
    @SerializedName("batch_id") val batchId: String = "",
    @SerializedName("batch_code") val batchCode: String = "",
    @SerializedName("high_score") val highScore: Int = 0,
    @SerializedName("exam_id") val examId: String = "",
    @SerializedName("exam_title") val examTitle: String = "",
    @SerializedName("trainer_id") val trainerId: String = ""
)