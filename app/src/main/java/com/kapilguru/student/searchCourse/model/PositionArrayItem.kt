package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PositionArrayItem(
    @SerializedName("total_no_of_students_trained")
    var totalNoOfStudentsTrained: Int = 0,
    @SerializedName("code")
    var code: String = "",
    @SerializedName("batchtype")
    var batchtype: String = "",
    @SerializedName("actual_fee")
    var actualFee: Double? = null,
    @SerializedName("course_title")
    var courseTitle: String = "",
    @SerializedName("fee")
    var fee: Double? = 0.0,
    @SerializedName("course_rating")
    var courseRating:Double? = null,
    @SerializedName("total_students_rated")
    var totalStudentsRated: Int = 0,
    @SerializedName("language")
    var language: String? = null,
    @SerializedName("trainers_year_of_exp")
    var trainersYearOfExp: Int = 0,
    @SerializedName("course_image")
    var courseImage: String = "",
    @SerializedName("course_badge_id")
    var courseBadgeId: Int? = -1,
    @SerializedName("duration_days")
    var durationDays: Int = 0,
    @SerializedName("course_video")
    var courseVideo: String? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("trainer_name")
    var trainerName: String = "",
    @SerializedName("trainer_id")
    var trainerId: Int = 0,
    @Expose(serialize = false,deserialize = false)
    var positionNumber: Int? = 7
)