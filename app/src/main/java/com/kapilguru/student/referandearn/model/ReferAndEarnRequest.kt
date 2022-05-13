package com.kapilguru.student.referandearn.model

import com.google.gson.annotations.SerializedName

data class ReferAndEarnRequest(
    @SerializedName("invitee_email") var inviteeEmail: String? = "",
    @SerializedName("invitee_contact_number") var inviteeContactNumber: String? = "",
    @SerializedName("referral_type") var referralType: String? = "",
    @SerializedName("refer_code") var referCode: String? = "",
    @SerializedName("user_id") var userId: Int? = null
)