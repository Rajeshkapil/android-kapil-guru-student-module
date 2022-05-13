package com.kapilguru.student.webinar.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class OnGoingWebinar (
    @SerializedName("db_ctime") var dbCtime: String? = "",
    ): ActiveWebinar() {


    fun toJSONObject(jsonObject: JSONObject): OnGoingWebinar {
        return OnGoingWebinar().apply {
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
            if (jsonObject.has("dbCtime")) {
                dbCtime = jsonObject.getString("dbCtime")
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
                price = jsonObject.getInt("no_of_attendees")
            }
            if (jsonObject.has("trainer_name")) {
                trainerName = jsonObject.getString("trainer_name")
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
        }
    }
}