package com.kapilguru.student.myClassRoomDetails.exam.model

import com.google.gson.annotations.SerializedName

data class QuestionPaperListResponse(
    @SerializedName("data") var questionPaperList: ArrayList<QuestionPaperListItemResData>?,
    @SerializedName("message") val message: String = "",
    @SerializedName("status") val status: Int = 0)