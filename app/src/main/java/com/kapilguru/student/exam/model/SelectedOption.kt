package com.kapilguru.student.exam.model


import com.google.gson.annotations.SerializedName

data class SelectedOption(
    @SerializedName("batch_id") var batchId: Int?,
    @SerializedName("correct_opt") var correctOpt: String?,
    @SerializedName("course_id") var courseId: Int?,
    @SerializedName("exam_id") var examId: Int?,
    @SerializedName("question_id") var questionId: Int?,
    @SerializedName("question_paper_id") var questionPaperId: Int?,
    @SerializedName("selected_opt") var selectedOpt: String?,
    @SerializedName("student_id") var studentId: Int?,
    @SerializedName("trainer_id") var trainerId: Int?
)