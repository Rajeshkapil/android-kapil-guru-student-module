package com.kapilguru.student.exam.model


import com.google.gson.annotations.SerializedName

data class QuestionsApi(
    @SerializedName("question_paper") var questionPaper: String?,
    @SerializedName("questions") var questions: String?,
    @SerializedName("responses") var responses: String?,
    @SerializedName("summary") var summary: String?
)

