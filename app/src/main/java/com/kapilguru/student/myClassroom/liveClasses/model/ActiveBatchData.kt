package com.kapilguru.student.myClassroom.liveClasses.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ActiveBatchData(
    @SerializedName("batch_id") var batchId: Int = -1,
    @SerializedName("course_id") var courseId: Int = -1,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("trainer_id") var trainerId: Int = -1,
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("batch_type") var batchType: String? = "",
    @SerializedName("start_date") var startDate: String? = "",
    @SerializedName("end_date") var endDate: String? = "",
    @SerializedName("batch_code") var batchCode: String? = "",
) : Parcelable {

    fun getObject(jsonObject : JSONObject) : ActiveBatchData{
        if(jsonObject.has("batch_id")){
            batchId = jsonObject.getInt("batch_id")
        }
        if(jsonObject.has("course_id")){
            courseId = jsonObject.getInt("course_id")
        }
        if(jsonObject.has("course_title")){
            courseTitle = jsonObject.getString("course_title")
        }
        if(jsonObject.has("trainer_id")){
            trainerId = jsonObject.getInt("trainer_id")
        }
        if(jsonObject.has("trainer_name")){
            trainerName = jsonObject.getString("trainer_name")
        }
        if(jsonObject.has("batch_type")){
            batchType = jsonObject.getString("batch_type")
        }
        if(jsonObject.has("start_date")){
            startDate = jsonObject.getString("start_date")
        }
        if(jsonObject.has("end_date")){
            endDate = jsonObject.getString("end_date")
        }
        if(jsonObject.has("batch_code")){
            batchCode = jsonObject.getString("batch_code")
        }
        return this
    }
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(batchId)
        parcel.writeInt(courseId)
        parcel.writeString(courseTitle)
        parcel.writeInt(trainerId)
        parcel.writeString(trainerName)
        parcel.writeString(batchType)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(batchCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActiveBatchData> {
        override fun createFromParcel(parcel: Parcel): ActiveBatchData {
            return ActiveBatchData(parcel)
        }

        override fun newArray(size: Int): Array<ActiveBatchData?> {
            return arrayOfNulls(size)
        }
    }
}
