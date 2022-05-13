package com.kapilguru.student.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsUploadRequest(
    @SerializedName("user_id") var userId : Int,
    @SerializedName("account_name") var accountName : String,
    @SerializedName("account_number") var accountNumber : String,
    @SerializedName("bank_name") var bankName : String,
    @SerializedName("branch_name") var branchName : String,
    @SerializedName("ifsc_code") var ifscCode : String)
