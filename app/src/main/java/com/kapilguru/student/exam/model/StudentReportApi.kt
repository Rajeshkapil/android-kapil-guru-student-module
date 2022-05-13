package com.kapilguru.student.exam.model


import com.google.gson.annotations.SerializedName

data class StudentReportApi(
    @SerializedName("student_results") var studentResults: String?,
    @SerializedName("summary") var summary: String?
)