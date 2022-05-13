package com.kapilguru.student.homeActivity.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.API_FORMAT_DATE_AND_TIME_WITH_T
import java.text.SimpleDateFormat
import java.util.*

data class UpComingScheduleApi(
    @SerializedName("start_time") var startTime: String? = "", //2021-11-25T13:00:00.000Z
    @SerializedName("code") var code: String? = "",
    @SerializedName("activity_type") var activityType: String? = "",
    @SerializedName("activity_id") var activityId: Int? = 0,
    @SerializedName("end_time") var endTime: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("activity_title") var activityTitle: String? = "",
    @SerializedName("activity_image") var activityImage: String? = "",
    @SerializedName("db_ctime") var dbTime: String? = "",
):Parcelable {
    val oneHourInMilliSec = 1 * 60 * 60 * 1000

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun isAboutToLiveInOneHour(): Boolean {
        val timeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
        val startTimeCalendar = Calendar.getInstance()
        val currentTime = Calendar.getInstance()
        startTimeCalendar.time = timeFormat.parse(startTime)
        currentTime.time = timeFormat.parse(dbTime)
        return startTimeCalendar.time.time - currentTime.time.time <= oneHourInMilliSec
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startTime)
        parcel.writeString(code)
        parcel.writeString(activityType)
        parcel.writeValue(activityId)
        parcel.writeString(endTime)
        parcel.writeValue(trainerId)
        parcel.writeString(activityTitle)
        parcel.writeString(dbTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpComingScheduleApi> {
        override fun createFromParcel(parcel: Parcel): UpComingScheduleApi {
            return UpComingScheduleApi(parcel)
        }

        override fun newArray(size: Int): Array<UpComingScheduleApi?> {
            return arrayOfNulls(size)
        }
    }

}