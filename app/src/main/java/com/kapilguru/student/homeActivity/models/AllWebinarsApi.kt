package com.kapilguru.student.homeActivity.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AllWebinarsApi(
    @SerializedName("end_date")
    val endDate: String? = "",
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("about")
    val about: String? = "",
    @SerializedName("video")
    val video: String? = null,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("rejected_by")
    val rejectedBy: Int = 0,
    @SerializedName("webinar_video")
    val webinarVideo: String? = null,
    @SerializedName("roomname")
    val roomname: String? = "",
    @SerializedName("rejected_reason")
    val rejectedReason: String? = "",
    @SerializedName("duration_hrs")
    val durationHrs: Int = 0,
    @SerializedName("price")
    val price: Double? = 0.0,
    @SerializedName("no_of_attendees")
    val noOfAttendees: Int = 0,
    @SerializedName("requested_date")
    val requestedDate: String? = "",
    @SerializedName("request_money_id")
    val requestMoneyId: Int = 0,
    @SerializedName("is_rejected")
    val isRejected: Int = 0,
    @SerializedName("registered_no_of_attendees")
    val registeredNoOfAttendees: Int = 0,
    @SerializedName("videomeet_host_id")
    val videomeetHostId: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("is_paid")
    val isPaid: Int = 0,
    @SerializedName("trainer_id")
    val trainerId: Int = 0,
    @SerializedName("start_date")
    val startDate: String? = "",
    @SerializedName("course_id")
    val courseId: Int = 0,
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("is_active")
    val isActive: Int = 0,
    @SerializedName("languages")
    val languages: String? = "",
    @SerializedName("speaker_name")
    val speakerName: String? = "",
    @SerializedName("end_time")
    val endTime: String? = "",
    @SerializedName("is_conducted")
    val isConducted: Int = 0,
    @SerializedName("room_pass")
    val roomPass: String? = "",
    @SerializedName("modified_date")
    val modifiedDate: String? = "",
    @SerializedName("is_verified")
    val isVerified: Int = 0,
    @SerializedName("created_by")
    val createdBy: Int = 0,
    @SerializedName("is_requested")
    val isRequested: Int = 0,
    @SerializedName("start_time")
    val startTime: String? = "",
    @SerializedName("verified_by")
    val verifiedBy: Int = 0,
    @SerializedName("confrenceid")
    val confrenceid: Int = 0,
    @SerializedName("duration_days")
    val durationDays: Int = 0,
    @SerializedName("modified_by")
    val modifiedBy: String? = "",
    @SerializedName("paid_money_id")
    val paidMoneyId: Int = 0,
    @SerializedName("topic")
    val topic: String? = "",
    @SerializedName("created_date")
    val createdDate: String? = "",
    @SerializedName("paid_date")
    val paidDate: String? = "",
    @SerializedName("status")
    val status: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(endDate)
        parcel.writeString(code)
        parcel.writeString(about)
        parcel.writeString(video)
        parcel.writeString(title)
        parcel.writeInt(rejectedBy)
        parcel.writeString(webinarVideo)
        parcel.writeString(roomname)
        parcel.writeString(rejectedReason)
        parcel.writeInt(durationHrs)
        parcel.writeValue(price)
        parcel.writeInt(noOfAttendees)
        parcel.writeString(requestedDate)
        parcel.writeInt(requestMoneyId)
        parcel.writeInt(isRejected)
        parcel.writeInt(registeredNoOfAttendees)
        parcel.writeInt(videomeetHostId)
        parcel.writeInt(id)
        parcel.writeInt(isPaid)
        parcel.writeInt(trainerId)
        parcel.writeString(startDate)
        parcel.writeInt(courseId)
        parcel.writeString(image)
        parcel.writeInt(isActive)
        parcel.writeString(languages)
        parcel.writeString(speakerName)
        parcel.writeString(endTime)
        parcel.writeInt(isConducted)
        parcel.writeString(roomPass)
        parcel.writeString(modifiedDate)
        parcel.writeInt(isVerified)
        parcel.writeInt(createdBy)
        parcel.writeInt(isRequested)
        parcel.writeString(startTime)
        parcel.writeInt(verifiedBy)
        parcel.writeInt(confrenceid)
        parcel.writeInt(durationDays)
        parcel.writeString(modifiedBy)
        parcel.writeInt(paidMoneyId)
        parcel.writeString(topic)
        parcel.writeString(createdDate)
        parcel.writeString(paidDate)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllWebinarsApi> {
        override fun createFromParcel(parcel: Parcel): AllWebinarsApi {
            return AllWebinarsApi(parcel)
        }

        override fun newArray(size: Int): Array<AllWebinarsApi?> {
            return arrayOfNulls(size)
        }
    }

    var languagesTextToShow = "" // collected text after languages api call
}
