package com.kapilguru.student.wallet.requestAmount

import com.google.gson.annotations.SerializedName

data class RequestMoneyApi(
    @SerializedName("refunds_amount")
    var refundsAmount: Double? = 0.0,
    @SerializedName("user_id")
    var userId: String = "",
    @SerializedName("total_amount")
    var totalAmount: Double? = 0.0,
    @SerializedName("deducted_amount")
    var deductedAmount: Double? = 0.0,
    @SerializedName("prizes_amount")
    var prizesAmount: Double? = 0.0,
    @SerializedName("payable_amount")
    var payableAmount: Double = 0.0,
    @SerializedName("deductions")
    var deductions: String = "",
    @SerializedName("referrals_amount")
    var referralsAmount: Double? = 0.0,
    @SerializedName("amount_from")
    var amountFrom: String = ""
)