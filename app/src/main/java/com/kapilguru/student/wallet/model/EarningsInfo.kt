package com.kapilguru.student.wallet.model


import com.google.gson.annotations.SerializedName

data class EarningsInfo(
    @SerializedName("prizes")
    var prizes: String?,
    @SerializedName("referrals")
    var referrals: String?,
    @SerializedName("refunds")
    var refunds: String?,
    @SerializedName("summary")
    var summary: String?
)