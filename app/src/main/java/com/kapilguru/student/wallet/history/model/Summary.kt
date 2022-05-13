package com.kapilguru.student.wallet.history.model

import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("request_money_id")
    val requestMoneyId: String = ""
)