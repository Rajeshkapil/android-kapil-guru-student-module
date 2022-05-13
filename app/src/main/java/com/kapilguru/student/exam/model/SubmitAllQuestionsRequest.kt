package com.kapilguru.student.exam.model

import com.google.gson.annotations.SerializedName

data class SubmitAllQuestionsRequest(
    @SerializedName("response_json") val responseJson: String = "")