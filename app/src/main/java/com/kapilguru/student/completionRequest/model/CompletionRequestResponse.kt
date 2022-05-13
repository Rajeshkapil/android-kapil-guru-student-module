package com.kapilguru.student.completionRequest.model

import com.google.gson.annotations.SerializedName

data class CompletionRequestResponse(
    @SerializedName("data") val completionReqList: ArrayList<CompletionRequestResData>? = null,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int) {
}