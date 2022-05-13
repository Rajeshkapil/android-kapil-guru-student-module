package com.kapilguru.student.exam.model


import com.google.gson.annotations.SerializedName

data class StudentReportResponse(
    @SerializedName("allData") var studentReportApi: StudentReportApi?,
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?
)