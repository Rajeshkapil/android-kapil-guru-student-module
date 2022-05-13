package com.kapilguru.student.ui.profile.data

import com.google.gson.annotations.SerializedName

data class CheckOTPRequest(
    @SerializedName("contact_number") var contactNumber : String?=null,
    @SerializedName("otp") var otp : String?=null,
    @SerializedName("email_id") var emailId : String?= null)
