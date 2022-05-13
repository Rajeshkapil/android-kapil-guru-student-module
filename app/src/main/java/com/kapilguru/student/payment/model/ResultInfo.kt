package com.kapilguru.student.payment.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ResultInfo(
    @SerializedName("resultStatus") var resultStatus: String? = "",
    @SerializedName("resultCode") var resultCode: String? = "",
    @SerializedName("resultMsg") var resultMsg: String? = "",
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun getObject(jsonObject: JSONObject): ResultInfo {
        this.resultStatus = jsonObject.optString("resultStatus")
        this.resultCode = jsonObject.optString("resultCode")
        this.resultMsg = jsonObject.optString("resultMsg")
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(resultStatus)
        parcel.writeString(resultCode)
        parcel.writeString(resultMsg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultInfo> {
        override fun createFromParcel(parcel: Parcel): ResultInfo {
            return ResultInfo(parcel)
        }

        override fun newArray(size: Int): Array<ResultInfo?> {
            return arrayOfNulls(size)
        }
    }
}