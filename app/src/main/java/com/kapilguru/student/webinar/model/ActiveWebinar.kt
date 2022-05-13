package com.kapilguru.student.webinar.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.languagesToShow
import org.json.JSONObject

open class ActiveWebinar(
    @SerializedName("end_date") var endDate: String? = "",
    @SerializedName("image") var image: String? = "",
    @SerializedName("languages") var languages: String? = "",
    @SerializedName("speaker_name") var speakerName: String? = "",
    @SerializedName("about") var about: String? = "",
    @SerializedName("video") var video: String? = "",
    @SerializedName("is_conducted") var isConducted: Int? = 0,
    @SerializedName("webinar_id") var webinarId: Int? = 0,
    @SerializedName("duration_hrs") var durationHrs: Int? = 0,
    @SerializedName("price") var price: Int? = 0,
    @SerializedName("no_of_attendees") var noOfAttendees: Int? = 0,
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("start_date") var startDate: String? = "", //2021-08-10 07:00:00
    @SerializedName("webinar_code") var webinarCode: String? = "",
    @SerializedName("webinar_title") var webinarTitle: String? = "",
) : Parcelable {
    var languagesToShow: String? = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        languagesToShow = parcel.readString()
    }

    fun toJSONObject(jsonObject: JSONObject, languagesDataList: ArrayList<LanguageData>): ActiveWebinar {
        return ActiveWebinar().apply {
            if (jsonObject.has("end_date")) {
                endDate = jsonObject.getString("end_date")
            }
            if (jsonObject.has("image")) {
                image = jsonObject.getString("image")
            }
            if (jsonObject.has("languages")) {
                languages = jsonObject.getString("languages")
            }
            if (jsonObject.has("speaker_name")) {
                speakerName = jsonObject.getString("speaker_name")
            }
            if (jsonObject.has("about")) {
                about = jsonObject.getString("about")
            }
            if (jsonObject.has("video")) {
                video = jsonObject.getString("video")
            }
            if (jsonObject.has("is_conducted")) {
                isConducted = jsonObject.getInt("is_conducted")
            }
            if (jsonObject.has("trainer_name")) {
                trainerName = jsonObject.getString("trainer_name")
            }
            if (jsonObject.has("webinar_id")) {
                webinarId = jsonObject.getInt("webinar_id")
            }
            if (jsonObject.has("duration_hrs")) {
                durationHrs = jsonObject.getInt("duration_hrs")
            }
            if (jsonObject.has("price")) {
                price = jsonObject.getInt("price")
            }
            if (jsonObject.has("no_of_attendees")) {
                noOfAttendees = jsonObject.getInt("no_of_attendees")
            }
            if (jsonObject.has("trainer_id")) {
                trainerId = jsonObject.getInt("trainer_id")
            }
            if (jsonObject.has("start_date")) {
                startDate = jsonObject.getString("start_date")
            }
            if (jsonObject.has("webinar_code")) {
                webinarCode = jsonObject.getString("webinar_code")
            }
            if (jsonObject.has("webinar_title")) {
                webinarTitle = jsonObject.getString("webinar_title")
            }
            languages?.let {
                languagesToShow = it.languagesToShow(languagesDataList)
            } ?: kotlin.run {
                languagesToShow = ""
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(endDate)
        parcel.writeString(image)
        parcel.writeString(languages)
        parcel.writeString(speakerName)
        parcel.writeString(about)
        parcel.writeString(video)
        parcel.writeValue(isConducted)
        parcel.writeValue(webinarId)
        parcel.writeValue(durationHrs)
        parcel.writeValue(price)
        parcel.writeValue(noOfAttendees)
        parcel.writeString(trainerName)
        parcel.writeValue(trainerId)
        parcel.writeString(startDate)
        parcel.writeString(webinarCode)
        parcel.writeString(webinarTitle)
        parcel.writeString(languagesToShow)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActiveWebinar> {
        override fun createFromParcel(parcel: Parcel): ActiveWebinar {
            return ActiveWebinar(parcel)
        }

        override fun newArray(size: Int): Array<ActiveWebinar?> {
            return arrayOfNulls(size)
        }
    }
}