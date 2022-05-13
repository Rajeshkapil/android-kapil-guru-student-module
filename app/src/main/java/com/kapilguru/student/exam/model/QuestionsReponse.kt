package com.kapilguru.student.exam.model


import com.google.gson.annotations.SerializedName

data class QuestionsReponse(
    @SerializedName("allData") var questionsApi: QuestionsApi?,
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?
)