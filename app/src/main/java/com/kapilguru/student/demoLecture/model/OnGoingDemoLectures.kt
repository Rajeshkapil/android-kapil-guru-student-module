package com.kapilguru.student.demoLecture.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class OnGoingDemoLectures(
    @SerializedName("db_ctime") var dbCtime: String? = "",
) : ActiveDemoLectures() {


    fun toJSONObject(jsonObject: JSONObject): OnGoingDemoLectures {
        return OnGoingDemoLectures().apply {
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
            if (jsonObject.has("isConducted")) {
                isConducted = jsonObject.getInt("isConducted")
            }
            if (jsonObject.has("duration")) {
                duration = jsonObject.getInt("duration")
            }
            if (jsonObject.has("no_of_attendees")) {
                noOfAttendees = jsonObject.getInt("no_of_attendees")
            }
            if (jsonObject.has("lecture_date")) {
                startDate = jsonObject.getString("lecture_date")
            }
            if (jsonObject.has("dbCtime")) {
                dbCtime = jsonObject.getString("dbCtime")
            }
            if (jsonObject.has("trainer_name")) {
                trainerName = jsonObject.getString("trainer_name")
            }
            if (jsonObject.has("trainer_id")) {
                trainerId = jsonObject.getInt("trainer_id")
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
            if (jsonObject.has("lecture_code")) {
                lectureCode = jsonObject.getString("lecture_code")
            }
        }
    }
}