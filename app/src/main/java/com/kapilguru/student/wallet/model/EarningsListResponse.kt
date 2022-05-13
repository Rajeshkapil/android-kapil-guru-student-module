package com.kapilguru.student.wallet.model


import com.google.gson.annotations.SerializedName

data class EarningsListResponse(
    @SerializedName("allData")
    var earningsInfo: EarningsInfo?,
    @SerializedName("data")
    var data: List<Any>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("status")
    var status: Int?
)