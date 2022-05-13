package com.kapilguru.student.demoLecture.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.languagesToShow
import com.kapilguru.student.webinar.model.LanguageData
import org.json.JSONObject

open class ActiveDemoLectures(
    @SerializedName("image") var image: String? = null,
    @SerializedName("languages") var languages: String? = "",
    @SerializedName("about") var about: String? = "",
    @SerializedName("video") var video: String? = "",
    @SerializedName("is_conducted") var isConducted: Int? = 0,
    @SerializedName("duration") var duration: Int? = 0,
    @SerializedName("no_of_attendees") var noOfAttendees: Int? = 0,
    @SerializedName("trainer_name") var trainerName: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("lecture_date") var startDate: String? = null, //2022-02-23 05:00:00
    @SerializedName("end_date") var endDate: String? = null, //2022-02-23 05:00:00
    @SerializedName("lecture_id") var lectureId: Int? = 0,
    @SerializedName("course_id") var courseId: Int? = 0,
    @SerializedName("course_name") var demoLectureName: String? = "",
    @SerializedName("lecture_title") var lectureTitle: String? = "",
    @SerializedName("lecture_code") var lectureCode: String? = "",
) : Parcelable {
    var languagesToShow : String? = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        languagesToShow = parcel.readString()
    }

    fun toJSONObject(jsonObject: JSONObject, languagesDataList: ArrayList<LanguageData>): ActiveDemoLectures {
        return ActiveDemoLectures().apply {
            if (jsonObject.has("image")) {
                image = jsonObject.getString("image")
            }
            if (jsonObject.has("languages")) {
                languages = jsonObject.getString("languages")
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
            if (jsonObject.has("duration")) {
                duration = jsonObject.getInt("duration")
            }
            if (jsonObject.has("no_of_attendees")) {
                noOfAttendees = jsonObject.getInt("no_of_attendees")
            }
            if (jsonObject.has("trainer_name")) {
                trainerName = jsonObject.getString("trainer_name")
            }
            if (jsonObject.has("trainer_id")) {
                trainerId = jsonObject.getInt("trainer_id")
            }
            if (jsonObject.has("lecture_date")) {
                startDate = jsonObject.getString("lecture_date")
            }
            if (jsonObject.has("lecture_id")) {
                lectureId = jsonObject.getInt("lecture_id")
            }
            if (jsonObject.has("course_id")) {
                courseId = jsonObject.getInt("course_id")
            }
            if (jsonObject.has("course_name")) {
                demoLectureName = jsonObject.getString("course_name")
            }
            if (jsonObject.has("lecture_title")) {
                lectureTitle = jsonObject.getString("lecture_title")
            }
            if (jsonObject.has("lecture_code")) {
                lectureCode = jsonObject.getString("lecture_code")
            }
            if (jsonObject.has("end_date")) {
                endDate = jsonObject.getString("end_date")
            }
            languages?.let {
                languagesToShow = it.languagesToShow(languagesDataList)
            } ?: kotlin.run {
                languagesToShow = ""
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(languages)
        parcel.writeString(about)
        parcel.writeString(video)
        parcel.writeValue(isConducted)
        parcel.writeValue(duration)
        parcel.writeValue(noOfAttendees)
        parcel.writeString(trainerName)
        parcel.writeValue(trainerId)
        parcel.writeString(startDate)
        parcel.writeValue(endDate)
        parcel.writeValue(lectureId)
        parcel.writeValue(courseId)
        parcel.writeString(demoLectureName)
        parcel.writeString(lectureCode)
        parcel.writeString(lectureTitle)
        parcel.writeString(languagesToShow)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActiveDemoLectures> {
        override fun createFromParcel(parcel: Parcel): ActiveDemoLectures {
            return ActiveDemoLectures(parcel)
        }

        override fun newArray(size: Int): Array<ActiveDemoLectures?> {
            return arrayOfNulls(size)
        }
    }
}