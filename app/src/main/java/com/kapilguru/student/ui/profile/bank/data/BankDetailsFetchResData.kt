package com.kapilguru.student.ui.profile.bank.data

import com.google.gson.annotations.SerializedName

data class BankDetailsFetchResData(
    @SerializedName("id") var bankUpdateId : Int?=null,
    @SerializedName("user_id") var userId : Int?=null,
    @SerializedName("account_name") var accountName : String?=null,
    @SerializedName("account_number") var accountNumber : String?=null,
    @SerializedName("bank_name") var bankName : String?=null,
    @SerializedName("branch_name") var branchName : String?=null,
    @SerializedName("ifsc_code") var ifscCode : String?=null,
    @SerializedName("is_active") var isActive : Int?=null,
    @SerializedName("created_by") var createdBy : Int?=null,
    @SerializedName("created_date") var createdDate : String?=null,
    @SerializedName("modified_by") var modifiedBy : Int?=null,
    @SerializedName("modified_date") var modifiedDate : String?=null)
