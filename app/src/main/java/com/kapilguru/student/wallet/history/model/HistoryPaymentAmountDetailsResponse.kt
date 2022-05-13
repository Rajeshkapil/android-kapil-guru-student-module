package com.kapilguru.student.wallet.history.model

import com.google.gson.annotations.SerializedName

data class HistoryPaymentAmountDetailsResponse(
    @SerializedName("summary")
    var summary: Summary,
    @SerializedName("refunds")
    var refunds: String? = null,
    @SerializedName("referrals")
    var referrals: String? = null,
    @SerializedName("payment")
    var payment: String? = null,
    @SerializedName("prizes")
    var prizes: String? = null
)