package com.kapilguru.student.ui.profile.bank.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.ui.profile.bank.data.BankDetailsFetchResData

data class BankDetailsFetchResponce(
    @SerializedName("data") val data: List<BankDetailsFetchResData>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)