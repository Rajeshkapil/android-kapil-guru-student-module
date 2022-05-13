package com.kapilguru.student.homeActivity.popularAndTrending

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PopularAndTrendingApi(
    @SerializedName("total_no_of_students_trained") var totalNoOfStudentsTrained: Int? = 0,
    @SerializedName("code") var code: String? = "",
    @SerializedName("is_active") var isActive: Int? = 0,
    @SerializedName("total_students_rated") var totalStudentsRated: Int? = 0,
    @SerializedName("actual_fee") var actualFee: Double? = 0.0,
    @SerializedName("course_title") var courseTitle: String? = "",
    @SerializedName("fee") var fee: Double? = 0.0,
    @SerializedName("course_rating") var courseRating: Double? = 0.0,
    @SerializedName("modified_date") var modifiedDate: String? = "",
    @SerializedName("course_image") var courseImage: String? = "",
    @SerializedName("trainers_year_of_exp") var trainersYearOfExp: Int? = 0,
    @SerializedName("is_verified") var isVerified: Int? = 0,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("duration_days") var durationDays: Int? = 0,
    @SerializedName("modified_by") var modifiedBy: String? = null,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("trainer_name") var trainerName: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(totalNoOfStudentsTrained)
        parcel.writeString(code)
        parcel.writeValue(isActive)
        parcel.writeValue(totalStudentsRated)
        parcel.writeValue(actualFee)
        parcel.writeString(courseTitle)
        parcel.writeValue(fee)
        parcel.writeValue(courseRating)
        parcel.writeString(modifiedDate)
        parcel.writeString(courseImage)
        parcel.writeValue(trainersYearOfExp)
        parcel.writeValue(isVerified)
        parcel.writeString(createdBy)
        parcel.writeValue(durationDays)
        parcel.writeString(modifiedBy)
        parcel.writeValue(id)
        parcel.writeString(createdDate)
        parcel.writeString(trainerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PopularAndTrendingApi> {
        override fun createFromParcel(parcel: Parcel): PopularAndTrendingApi {
            return PopularAndTrendingApi(parcel)
        }

        override fun newArray(size: Int): Array<PopularAndTrendingApi?> {
            return arrayOfNulls(size)
        }
    }
}