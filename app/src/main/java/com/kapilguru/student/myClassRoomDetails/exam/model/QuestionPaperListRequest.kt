package com.kapilguru.student.myClassRoomDetails.exam.model

import com.google.gson.annotations.SerializedName

data class QuestionPaperListRequest(
    @SerializedName("batch_id") val batchId: Int = 0,
    @SerializedName("student_id") val studentId: String = "")