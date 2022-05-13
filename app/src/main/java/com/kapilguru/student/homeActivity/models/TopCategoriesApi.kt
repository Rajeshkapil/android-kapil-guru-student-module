package com.kapilguru.student.homeActivity.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TopCategoriesApi(
    @SerializedName("category_image") var categoryImage: String? = null,
    @SerializedName("category_name") var categoryName: String? = "",
    @SerializedName("is_active") var isActive: Int? = 0,
    @SerializedName("code") var code: String? = "",
    @SerializedName("modified_by") var modifiedBy: String? = null,
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("created_date") var createdDate: String? = "",
    @SerializedName("modified_date") var modifiedDate: String? = "",
    @SerializedName("created_by") var createdBy: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryImage)
        parcel.writeString(categoryName)
        parcel.writeValue(isActive)
        parcel.writeString(code)
        parcel.writeString(modifiedBy)
        parcel.writeValue(id)
        parcel.writeString(createdDate)
        parcel.writeString(modifiedDate)
        parcel.writeString(createdBy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopCategoriesApi> {
        override fun createFromParcel(parcel: Parcel): TopCategoriesApi {
            return TopCategoriesApi(parcel)
        }

        override fun newArray(size: Int): Array<TopCategoriesApi?> {
            return arrayOfNulls(size)
        }
    }
}