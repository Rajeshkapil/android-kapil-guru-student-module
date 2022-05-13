package com.kapilguru.student.wallet.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class EarningsReferralList(
    @SerializedName("invitee_email")
    var inviteeEmail: String? = "",
    @SerializedName("referral_name")
    var referralName: String? = "",
    @SerializedName("referred_to")
    var referredTo: String? = "",
    @SerializedName("user_id")
    var userId: Int? = 0,
    @SerializedName("subscription_amount")
    var subscriptionAmount: Double? = 0.0,
    @SerializedName("invitee_contact_number")
    var inviteeContactNumber: String? = "",
    @SerializedName("referred_date")
    var referredDate: String? = "",
    @SerializedName("referral_id")
    var referralId: Int? = 0,
    @SerializedName("referral_amount")
    var referralAmount: Double? = 0.0,
    @SerializedName("referral_type")
    var referralType: String? = "",
    @SerializedName("subscription_date")
    var subscriptionDate: String? = ""
):Parcelable{



    fun getEarningsReferralList(jsonstr: JSONObject): EarningsReferralList {
        return EarningsReferralList().apply {
            userId = jsonstr.getInt("user_id")
            referralId = jsonstr.getInt("referral_id")
            referralName = jsonstr.getString("referral_name")
            referredDate = jsonstr.getString("referred_date")
            referredTo = jsonstr.getString("referred_to")
            subscriptionDate = jsonstr.getString("subscription_date")
            subscriptionAmount = jsonstr.getDouble("subscription_amount")
            referralAmount = jsonstr.getDouble("referral_amount")
//            inviteeEmail = jsonstr.getString("invitee_email")
//            inviteeContactNumber = jsonstr.getString("invitee_contact_number")
//            referralType = jsonstr.getString("referral_type")
        }
    }


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(inviteeEmail)
        parcel.writeString(referralName)
        parcel.writeString(referredTo)
        parcel.writeValue(userId)
        parcel.writeValue(subscriptionAmount)
        parcel.writeString(inviteeContactNumber)
        parcel.writeString(referredDate)
        parcel.writeValue(referralId)
        parcel.writeValue(referralAmount)
        parcel.writeString(referralType)
        parcel.writeString(subscriptionDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarningsReferralList> {
        override fun createFromParcel(parcel: Parcel): EarningsReferralList {
            return EarningsReferralList(parcel)
        }

        override fun newArray(size: Int): Array<EarningsReferralList?> {
            return arrayOfNulls(size)
        }
    }

}