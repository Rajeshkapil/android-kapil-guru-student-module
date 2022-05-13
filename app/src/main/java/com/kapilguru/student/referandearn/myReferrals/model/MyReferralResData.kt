package com.kapilguru.student.referandearn.myReferrals.model

import com.google.gson.annotations.SerializedName

data class MyReferralResData(
    @SerializedName("subscription_package") val subscriptionPackage: String? = null,
    @SerializedName("subscription_date") val subscriptionDate: String? = null,
    @SerializedName("subscription_amount") val subscriptionAmount: Int = 0,
    @SerializedName("requested_date") val requestedDate: String? = null,
    @SerializedName("request_money_id") val requestMoneyId: Int = 0,
    @SerializedName("referral_amount_paid") val referralAmountPaid: Int = 0,
    @SerializedName("referral_amount_paid_date") val referralAmountPaidDate: String? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("is_paid") val isPaid: Int = 0,
    @SerializedName("referral_type") val referralType: String = "",
    @SerializedName("is_active") val isActive: Int = 0,
    @SerializedName("invitee_contact_number") val inviteeContactNumber: String = "",
    @SerializedName("referral_expiry_date") val referralExpiryDate: String? = null,  //2022-01-03T06:01:37.000Z
    @SerializedName("modified_date") val modifiedDate: String? = null,
    @SerializedName("created_by") val createdBy: Int = 0,
    @SerializedName("is_used") val isUsed: Int = 0,
    @SerializedName("is_requested") val isRequested: Int = 0,
    @SerializedName("invitee_email") val inviteeEmail: String = "",
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("paid_money_id") val paidMoneyId: Int = 0,
    @SerializedName("modified_by") val modifiedBy: String? = null,
    @SerializedName("created_date") val createdDate: String = "",  //2021-12-29T06:01:37.000Z
    @SerializedName("subscription_type") val subscriptionType: String? = null,
    @SerializedName("invitee_user_id") val inviteeUserId: Int? = null,
    @SerializedName("referral_amount_earned") val referralAmountEarned: Double? = null,
    @SerializedName("refer_code") val referCode: String = ""
)