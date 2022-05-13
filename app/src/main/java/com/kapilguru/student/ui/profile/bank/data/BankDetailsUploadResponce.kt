package com.kapilguru.student.ui.profile.bank.data

import com.google.gson.annotations.SerializedName
import com.kapilguru.student.ui.profile.bank.data.BankDetailsUploadResData

data class BankDetailsUploadResponce(
    @SerializedName("data") val bankDetailsUploadResData: BankDetailsUploadResData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)