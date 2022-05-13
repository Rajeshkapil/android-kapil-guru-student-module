package com.kapilguru.student.ui.profile.data

import com.google.gson.annotations.SerializedName

data class GetOTPRequest(
    @SerializedName("contact_number") var contactNumber : String?=null,
    @SerializedName("email_id") var emailId : String?=null,
    @SerializedName("user_id") var userId : String?=null,
    )