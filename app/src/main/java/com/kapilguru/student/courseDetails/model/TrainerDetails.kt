package com.kapilguru.student.courseDetails.model

import android.os.Parcel
import android.os.Parcelable

data class TrainerDetails(
    var aboutTrainer: String? = "",
    var trainerName: String? = "",
    var trainersYearOfExp: Int? = 0,
    var totalNoOfStudentsTrained: Int? = 0
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(aboutTrainer)
        parcel.writeString(trainerName)
        parcel.writeValue(trainersYearOfExp)
        parcel.writeValue(totalNoOfStudentsTrained)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrainerDetails> {
        override fun createFromParcel(parcel: Parcel): TrainerDetails {
            return TrainerDetails(parcel)
        }

        override fun newArray(size: Int): Array<TrainerDetails?> {
            return arrayOfNulls(size)
        }
    }

}