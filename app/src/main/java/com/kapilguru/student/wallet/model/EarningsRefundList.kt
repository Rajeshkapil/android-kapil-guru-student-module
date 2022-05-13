package com.kapilguru.student.wallet.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class EarningsRefundList(
    @SerializedName("batch_code")
    var batchCode: String?=null,
    @SerializedName("batch_id")
    var batchId: Int?=null,
    @SerializedName("course_title")
    var courseTitle: String?=null,
    @SerializedName("end_date")
    var endDate: String?=null,
    @SerializedName("enrollment_date")
    var enrollmentDate: String?=null,
    @SerializedName("start_date")
    var startDate: String?=null,
    @SerializedName("total_fee")
    var totalFee: Double?=null,
    @SerializedName("trainer_code")
    var trainerCode: String?=null,
    @SerializedName("trainer_id")
    var trainerId: Int?=null,
    @SerializedName("trainer_name")
    var trainerName: String?=null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    fun getEarningsRefundList(jsonstr: JSONObject): EarningsRefundList {
        return EarningsRefundList().apply {
            batchId = jsonstr.getInt("batch_id")
            trainerId = jsonstr.getInt("trainer_id")
            trainerCode = jsonstr.getString("trainer_code")
            trainerName = jsonstr.getString("trainer_name")
            courseTitle = jsonstr.getString("course_title")
            enrollmentDate = jsonstr.getString("enrollment_date")
            totalFee = jsonstr.getDouble("total_fee")
            startDate = jsonstr.getString("start_date")
            endDate = jsonstr.getString("end_date")
//            batchCode = jsonstr.getString("batch_code")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(batchCode)
        parcel.writeValue(batchId)
        parcel.writeString(courseTitle)
        parcel.writeString(endDate)
        parcel.writeString(enrollmentDate)
        parcel.writeString(startDate)
        parcel.writeValue(totalFee)
        parcel.writeString(trainerCode)
        parcel.writeValue(trainerId)
        parcel.writeString(trainerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EarningsRefundList> {
        override fun createFromParcel(parcel: Parcel): EarningsRefundList {
            return EarningsRefundList(parcel)
        }

        override fun newArray(size: Int): Array<EarningsRefundList?> {
            return arrayOfNulls(size)
        }
    }

}