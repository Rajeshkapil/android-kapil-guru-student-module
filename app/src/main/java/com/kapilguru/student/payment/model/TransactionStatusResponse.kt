package com.kapilguru.student.payment.model

import com.google.gson.annotations.SerializedName

data class TransactionStatusResponse(
    @SerializedName("data") val transactionStatusResponseData: TransactionStatusResponseData?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)
