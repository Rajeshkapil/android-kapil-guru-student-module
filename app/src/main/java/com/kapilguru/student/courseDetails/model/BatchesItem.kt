package com.kapilguru.student.courseDetails.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BatchesItem(
    @SerializedName("end_date")
    var endDate: String?=null,
    @SerializedName("code")
    var code: String?=null,
    @SerializedName("class_duration")
    var classDuration: Int = 0,
    @SerializedName("current_no_of_students")
    var currentNoOfStudents: Int = 0,
    @SerializedName("day_sat")
    var daySat: Int = 0,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("max_no_of_students")
    var maxNoOfStudents: Int = 0,
    @SerializedName("discounted_price")
    var discountedPrice: Double? = null,
    @SerializedName("day_thu")
    var dayThu: Int = 0,
    @SerializedName("no_of_days")
    var noOfDays: Int = 0,
    @SerializedName("batch_type")
    var batchType: String?=null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("trainer_id")
    var trainerId: Int = 0,
    @SerializedName("start_date")
    var startDate: String?=null,
    @SerializedName("course_id")
    var courseId: Int = 0,
    @SerializedName("day_mon")
    var dayMon: Int = 0,
    @SerializedName("day_sun")
    var daySun: Int = 0,
    @SerializedName("is_batch_filled")
    var isBatchFilled: Int = 0,
    @SerializedName("end_time")
    var endTime: String? = null,
    @SerializedName("day_tue")
    var dayTue: Int = 0,
    @SerializedName("time_zone")
    var timeZone: String?=null,
    @SerializedName("start_time")
    var startTime: String? = null,
    @SerializedName("day_fri")
    var dayFri: Int = 0,
    @SerializedName("day_wed")
    var dayWed: Int = 0,
    @SerializedName("batch_price")
    var batchPrice: Double? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(endDate)
        parcel.writeString(code)
        parcel.writeInt(classDuration)
        parcel.writeInt(currentNoOfStudents)
        parcel.writeInt(daySat)
        parcel.writeString(description)
        parcel.writeInt(maxNoOfStudents)
        parcel.writeValue(discountedPrice)
        parcel.writeInt(dayThu)
        parcel.writeInt(noOfDays)
        parcel.writeString(batchType)
        parcel.writeInt(id)
        parcel.writeInt(trainerId)
        parcel.writeString(startDate)
        parcel.writeInt(courseId)
        parcel.writeInt(dayMon)
        parcel.writeInt(daySun)
        parcel.writeInt(isBatchFilled)
        parcel.writeString(endTime)
        parcel.writeInt(dayTue)
        parcel.writeString(timeZone)
        parcel.writeString(startTime)
        parcel.writeInt(dayFri)
        parcel.writeInt(dayWed)
        parcel.writeValue(batchPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchesItem> {
        override fun createFromParcel(parcel: Parcel): BatchesItem {
            return BatchesItem(parcel)
        }

        override fun newArray(size: Int): Array<BatchesItem?> {
            return arrayOfNulls(size)
        }
    }
} 
