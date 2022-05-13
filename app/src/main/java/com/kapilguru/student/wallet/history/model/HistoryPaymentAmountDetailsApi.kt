package com.kapilguru.student.wallet.history.model

import com.google.gson.annotations.SerializedName

data class HistoryPaymentAmountDetailsApi(
    @SerializedName("allData")
    val historyPaymentAmountDetailsResponse: HistoryPaymentAmountDetailsResponse,
    @SerializedName("data")
    val data: List<Object>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)