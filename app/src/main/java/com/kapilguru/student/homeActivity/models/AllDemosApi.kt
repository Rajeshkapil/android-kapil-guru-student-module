package com.kapilguru.student.homeActivity.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AllDemosApi(
    @SerializedName("code") var code: String? = "",
    @SerializedName("lecture_date") var lectureDate: String? = "",
    @SerializedName("end_date") var endDate: String? = "",
    @SerializedName("about") var about: String? = "",
    @SerializedName("video") var video: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("rejected_by") var rejectedBy: Int = 0,
    @SerializedName("duration") var duration: Int = 0,
    @SerializedName("no_of_days") var noOfDays: Int = 0,
    @SerializedName("roomname") var roomname:  String? = null,
    @SerializedName("rejected_reason") var rejectedReason:  String? = null,
    @SerializedName("no_of_attendees") var noOfAttendees: Int = 0,
    @SerializedName("is_rejected") var isRejected: Int = 0,
    @SerializedName("registered_no_of_attendees") var registeredNoOfAttendees: Int = 0,
    @SerializedName("videomeet_host_id") var videomeetHostId: Int = 0,
    @SerializedName("id") var id: Int = 0,
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("trainer_id") var trainerId: Int = 0,
    @SerializedName("course_id") var courseId: Int = 0,
    @SerializedName("image") var image: String? = "",
    @SerializedName("is_active") var isActive: Int = 0,
    @SerializedName("languages") var languages: String? = "",
    @SerializedName("room_pass") var roomPass:  String? = null,
    @SerializedName("modified_date") var modifiedDate: String? = "",
    @SerializedName("is_verified") var isVerified: Int = 0,
    @SerializedName("created_by") var createdBy: Int = 0,
    @SerializedName("start_time") var startTime:  String? = null,
    @SerializedName("verified_by") var verifiedBy: Int = 0,
    @SerializedName("confrenceid") var confrenceid: Int = 0,
    @SerializedName("meeting_url") var meetingUrl:  String? = null,
    @SerializedName("modified_by") var modifiedBy:  String? = null,
    @SerializedName("lecture_video") var lectureVideo: String? = "",
    @SerializedName("topic") var topic:  String? = null,
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("status") var status: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(lectureDate)
        parcel.writeString(endDate)
        parcel.writeString(about)
        parcel.writeString(video)
        parcel.writeString(title)
        parcel.writeInt(rejectedBy)
        parcel.writeInt(duration)
        parcel.writeInt(duration)
        parcel.writeString(roomname)
        parcel.writeString(rejectedReason)
        parcel.writeInt(noOfAttendees)
        parcel.writeInt(isRejected)
        parcel.writeInt(registeredNoOfAttendees)
        parcel.writeInt(videomeetHostId)
        parcel.writeInt(id)
        parcel.writeString(trainerName)
        parcel.writeInt(trainerId)
        parcel.writeInt(courseId)
        parcel.writeString(image)
        parcel.writeInt(isActive)
        parcel.writeString(languages)
        parcel.writeString(roomPass)
        parcel.writeString(modifiedDate)
        parcel.writeInt(isVerified)
        parcel.writeInt(createdBy)
        parcel.writeString(startTime)
        parcel.writeInt(verifiedBy)
        parcel.writeInt(confrenceid)
        parcel.writeString(meetingUrl)
        parcel.writeString(modifiedBy)
        parcel.writeString(lectureVideo)
        parcel.writeString(topic)
        parcel.writeString(createdDate)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllDemosApi> {
        override fun createFromParcel(parcel: Parcel): AllDemosApi {
            return AllDemosApi(parcel)
        }

        override fun newArray(size: Int): Array<AllDemosApi?> {
            return arrayOfNulls(size)
        }
    }

    var languagesTextToShow = "" // collected text after languages api call
}