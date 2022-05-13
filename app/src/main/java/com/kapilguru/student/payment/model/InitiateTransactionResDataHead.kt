package com.kapilguru.student.payment.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class InitiateTransactionResDataHead(
    @SerializedName("responseTimestamp") var responseTimestamp: String? = "",
    @SerializedName("version") var version: String? = "",
    @SerializedName("signature") var signature: String? = "",
):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getObject(jsonObject: JSONObject): InitiateTransactionResDataHead {
        this.responseTimestamp = jsonObject.optString("responseTimestamp")
        this.version = jsonObject.optString("version")
        this.signature = jsonObject.optString("signature")
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(responseTimestamp)
        parcel.writeString(version)
        parcel.writeString(signature)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InitiateTransactionResDataHead> {
        override fun createFromParcel(parcel: Parcel): InitiateTransactionResDataHead {
            return InitiateTransactionResDataHead(parcel)
        }

        override fun newArray(size: Int): Array<InitiateTransactionResDataHead?> {
            return arrayOfNulls(size)
        }
    }
}