package com.kapilguru.student.wallet.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EarningsSummary(
    @SerializedName("prizes_amount_available")
    var prizesAmountAvailable: Double?=null,
    @SerializedName("referral_amount_available")
    var referralAmountAvailable: Double?=null,
    @SerializedName("refund_amount_available")
    var refundAmountAvailable: Double?=null,
    @SerializedName("user_id")
    var userId: Int?=null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(prizesAmountAvailable)
        parcel.writeValue(referralAmountAvailable)
        parcel.writeValue(refundAmountAvailable)
        parcel.writeValue(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarningsSummary> {
        override fun createFromParcel(parcel: Parcel): EarningsSummary {
            return EarningsSummary(parcel)
        }

        override fun newArray(size: Int): Array<EarningsSummary?> {
            return arrayOfNulls(size)
        }
    }
}