package com.kapilguru.student.payment.model

import android.os.Parcel
import android.os.Parcelable

class BatchDays(
    var mon: Int? = 0, var tue: Int? = 0, var wed: Int? = 0,
    var thu: Int? = 0, var fri: Int? = 0, var sat: Int? = 0,
    var sun: Int? = 0,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(mon)
        parcel.writeValue(tue)
        parcel.writeValue(wed)
        parcel.writeValue(thu)
        parcel.writeValue(fri)
        parcel.writeValue(sat)
        parcel.writeValue(sun)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BatchDays> {
        override fun createFromParcel(parcel: Parcel): BatchDays {
            return BatchDays(parcel)
        }

        override fun newArray(size: Int): Array<BatchDays?> {
            return arrayOfNulls(size)
        }
    }
}