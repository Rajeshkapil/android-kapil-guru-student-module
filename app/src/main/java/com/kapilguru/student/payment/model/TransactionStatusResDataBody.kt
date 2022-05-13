package com.kapilguru.student.payment.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.payment.model.ResultInfo

data class TransactionStatusResDataBody(
    @SerializedName("resultInfo") var resultInfo : ResultInfo? = null,
    @SerializedName("txnId") var txnId : String? = "",
    @SerializedName("bankTxnId") var bankTxnId  : String? = "",
    @SerializedName("orderId") var orderId : String? = "",
    @SerializedName("txnAmount") var txnAmount : String? = "",
    @SerializedName("txnType") var txnType : String? = "",
    @SerializedName("gatewayName") var gatewayName : String? = "",
    @SerializedName("bankName") var bankName : String? = "",
    @SerializedName("mid") var mid : String? = "",
    @SerializedName("paymentMode") var paymentMode : String? = "",
    @SerializedName("refundAmt") var refundAmt : String? = "",
    @SerializedName("txnDate") var txnDate  : String? = "",  //format : 2021-11-23 12:07:52.0
    @SerializedName("cardScheme") var cardScheme  : String? = "",
    @SerializedName("rrnCode") var rrnCode  : String? = "",
    @SerializedName("authCode") var authCode  : String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ResultInfo::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(resultInfo, flags)
        parcel.writeString(txnId)
        parcel.writeString(bankTxnId)
        parcel.writeString(orderId)
        parcel.writeString(txnAmount)
        parcel.writeString(txnType)
        parcel.writeString(gatewayName)
        parcel.writeString(bankName)
        parcel.writeString(mid)
        parcel.writeString(paymentMode)
        parcel.writeString(refundAmt)
        parcel.writeString(txnDate)
        parcel.writeString(cardScheme)
        parcel.writeString(rrnCode)
        parcel.writeString(authCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionStatusResDataBody> {
        override fun createFromParcel(parcel: Parcel): TransactionStatusResDataBody {
            return TransactionStatusResDataBody(parcel)
        }

        override fun newArray(size: Int): Array<TransactionStatusResDataBody?> {
            return arrayOfNulls(size)
        }
    }
}