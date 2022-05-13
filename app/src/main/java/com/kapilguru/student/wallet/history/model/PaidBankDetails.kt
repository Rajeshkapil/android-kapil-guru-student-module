package com.kapilguru.student.wallet.history.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class PaidBankDetails(
    @SerializedName("ifsc_code")
    var ifscCode: String?=null,
    @SerializedName("account_number")
    var accountNumber: String?=null,
    @SerializedName("user_id")
    var userId: Int ?= 0,
    @SerializedName("request_money_id")
    var requestMoneyId: Double ?= 0.0,
    @SerializedName("account_name")
    var accountName: String?=null,
    @SerializedName("branch_name")
    var branchName: String?=null,
    @SerializedName("paid_money_id")
    var paidMoneyId: Double ?= 0.0,
    @SerializedName("paid_amount")
    var paidAmount: Double ?= 0.0,
    @SerializedName("bank_name")
    var bankName: String?=null,
    @SerializedName("transaction_summary")
    var transactionSummary: String?=null,
    @SerializedName("paid_date")
    var paidDate: String?=null,
    @SerializedName("remarks")
    var remarks: String?=null
) {



    fun getPaidBankDetails(json: String?) :PaidBankDetails {
        json?.let {
            val jsonObject = JSONObject(it)
            return PaidBankDetails().apply {
                ifscCode = jsonObject.getString("ifsc_code")
                accountNumber = jsonObject.getString("account_number")
                userId = jsonObject.getInt("user_id")
                requestMoneyId = jsonObject.getDouble("request_money_id")
                accountName = jsonObject.getString("account_name")
                branchName = jsonObject.getString("branch_name")
                paidMoneyId = jsonObject.getDouble("paid_money_id")
                paidAmount = jsonObject.getDouble("paid_amount")
                bankName = jsonObject.getString("bank_name")
                transactionSummary = jsonObject.getString("transaction_summary")
                paidDate = jsonObject.getString("paid_date")
                remarks = jsonObject.getString("remarks")
            }
        }?: kotlin.run {
            return PaidBankDetails()
        }

    }

}