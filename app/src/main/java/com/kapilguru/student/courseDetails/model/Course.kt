package com.kapilguru.student.courseDetails.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("batchtype")
    var batchtype: String? = "",
    @SerializedName("discount_amount")
    var discountAmount: Double? = 0.0,
    @SerializedName("fee")
    var fee: Double? = 0.0,
    @SerializedName("course_rating")
    var courseRating: Double? = 0.0,
    @SerializedName("syllabus_type")
    var syllabusType: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("language")
    var language: String? = "",
    @SerializedName("syllabus_text_content")
    var syllabusTextContent: String? = "",
    @SerializedName("course_image")
    var courseImage: String? = "",
    @SerializedName("course_reviews")
    var courseReviews: String? = "",
    @SerializedName("trainers_year_of_exp")
    var trainersYearOfExp: Int = 0,
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("about_trainer")
    var aboutTrainer: String? = "",
    @SerializedName("course_badge_id")
    var courseBadgeId: String? = null,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("trainer_name")
    var trainerName: String? = "",
    @SerializedName("trainer_id")
    var trainerId: Int? = 0,
    @SerializedName("total_no_of_students_trained")
    var totalNoOfStudentsTrained: Int = 0,
    @SerializedName("actual_fee")
    var actualFee: Double? = 0.0,
    @SerializedName("course_title")
    var courseTitle: String? = "",
    @SerializedName("is_verified")
    var isVerified: Int? = 0,
    @SerializedName("demo_video")
    var demoVideo: String? = "",
    @SerializedName("tags")
    var tags: String? = "",
    @SerializedName("course_sub_title")
    var courseSubTitle: String? = "",
    @SerializedName("verified_date")
    var verifiedDate: String? = "",
    @SerializedName("verified_by")
    var verifiedBy: String? = "",
    @SerializedName("duration_days")
    var durationDays: Int? = 0,
    @SerializedName("course_video")
    var courseVideo: String? = "",
    @SerializedName("discount_percent")
    var discountPercent: String? = "",
    @SerializedName("syllabus_attachment")
    var syllabusAttachment: String? = "" ,
    @SerializedName("students_rated")
    var studentsRated: String? = "" ,
    @SerializedName("contact")
    var contact: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(batchtype)
        parcel.writeValue(discountAmount)
        parcel.writeValue(fee)
        parcel.writeValue(courseRating)
        parcel.writeString(syllabusType)
        parcel.writeString(description)
        parcel.writeString(language)
        parcel.writeString(syllabusTextContent)
        parcel.writeString(courseImage)
        parcel.writeString(courseReviews)
        parcel.writeInt(trainersYearOfExp)
        parcel.writeInt(categoryId)
        parcel.writeString(aboutTrainer)
        parcel.writeString(courseBadgeId)
        parcel.writeValue(id)
        parcel.writeString(trainerName)
        parcel.writeValue(trainerId)
        parcel.writeInt(totalNoOfStudentsTrained)
        parcel.writeValue(actualFee)
        parcel.writeString(courseTitle)
        parcel.writeValue(isVerified)
        parcel.writeString(demoVideo)
        parcel.writeString(tags)
        parcel.writeString(courseSubTitle)
        parcel.writeString(verifiedDate)
        parcel.writeString(verifiedBy)
        parcel.writeValue(durationDays)
        parcel.writeString(courseVideo)
        parcel.writeString(discountPercent)
        parcel.writeString(syllabusAttachment)
        parcel.writeString(studentsRated)
        parcel.writeString(contact)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {
        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }
}