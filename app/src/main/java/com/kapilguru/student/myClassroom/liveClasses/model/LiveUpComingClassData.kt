package com.kapilguru.student.myClassroom.liveClasses.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class LiveUpComingClassData(
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("batch_id") var batchId: Int = 0,
    @SerializedName("session_no") var sessionNo: Int = 0,
    @SerializedName("end_time") var endTime: String? = "",
    @SerializedName("db_ctime") var dbCtime: String? = "",
    @SerializedName("start_time") var startTime: String? = "",
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("batch_type") var batchType: String? = "",
    @SerializedName("batch_code") var batchCode: String? = "",
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("room_name") var roomName: String? = "",
    var batchData: ActiveBatchData? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ActiveBatchData::class.java.classLoader)
    )

    fun getObject(jsonObject: JSONObject): LiveUpComingClassData {
        if (jsonObject.has("course_id")) {
            courseId = jsonObject.getInt("course_id")
        }
        if (jsonObject.has("batch_id")) {
            batchId = jsonObject.getInt("batch_id")
        }
        if (jsonObject.has("session_no")) {
            sessionNo = jsonObject.getInt("session_no")
        }
        if (jsonObject.has("end_time")) {
            endTime = jsonObject.getString("end_time")
        }
        if (jsonObject.has("db_ctime")) {
            dbCtime = jsonObject.getString("db_ctime")
        }
        if (jsonObject.has("start_time")) {
            startTime = jsonObject.getString("start_time")
        }
        if (jsonObject.has("trainer_id")) {
            trainerId = jsonObject.getInt("trainer_id")
        }
        if (jsonObject.has("course_title")) {
            courseTitle = jsonObject.getString("course_title")
        }
        if (jsonObject.has("trainer_name")) {
            trainerName = jsonObject.getString("trainer_name")
        }
        if (jsonObject.has("batch_type")) {
            batchType = jsonObject.getString("batch_type")
        }
        if (jsonObject.has("batch_code")) {
            batchCode = jsonObject.getString("batch_code")
        }
        if (jsonObject.has("created_date")) {
            createdDate = jsonObject.getString("created_date")
        }
        if (jsonObject.has("room_name")) {
            roomName = jsonObject.getString("room_name")
        }
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(courseId)
        parcel.writeInt(batchId)
        parcel.writeInt(sessionNo)
        parcel.writeString(endTime)
        parcel.writeString(dbCtime)
        parcel.writeString(startTime)
        parcel.writeInt(trainerId)
        parcel.writeString(courseTitle)
        parcel.writeString(trainerName)
        parcel.writeString(batchType)
        parcel.writeString(batchCode)
        parcel.writeString(createdDate)
        parcel.writeString(roomName)
        parcel.writeParcelable(batchData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveUpComingClassData> {
        override fun createFromParcel(parcel: Parcel): LiveUpComingClassData {
            return LiveUpComingClassData(parcel)
        }

        override fun newArray(size: Int): Array<LiveUpComingClassData?> {
            return arrayOfNulls(size)
        }
    }
}