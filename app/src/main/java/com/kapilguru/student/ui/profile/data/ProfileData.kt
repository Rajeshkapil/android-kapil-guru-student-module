package com.kapilguru.student.ui.profile.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileData(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("contact_number") var contactNumber: String? = null,
    @SerializedName("country_code") var countryCode: String? = null,
    @SerializedName("alternate_number") var alternateNumber: String? = null,
    @SerializedName("email_id") var email_id: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("address_line_1") var addressLine1: String? = null,
    @SerializedName("address_line_2") var addressLine2: String? = null,
    @SerializedName("city") var cityId: String? = null,
    @SerializedName("state") var stateId: String? = null,
    @SerializedName("country") var countryId: String? = null,
    @SerializedName("postal_code") var postalCode: String? = null,
    @SerializedName("describe_about_you") var description: String? = null,
    @SerializedName("is_organization") var isOrganization: Int? = null,
    @SerializedName("company_name") var companyName: String? = null,
    @SerializedName("org_contact_number") var orgContactNumber: String? = null,
    @SerializedName("official_email") var officialEmail: String? = null,
    @SerializedName("company_website_url") var companyWebsiteUrl: String? = null,
    @SerializedName("org_address_line_1") var orgAddressLine1: String? = null,
    @SerializedName("org_address_line_2") var orgAddressLine2: String? = null,
    @SerializedName("org_city") var orgCityId: String? = null,
    @SerializedName("org_state") var orgStateId: String? = null,
    @SerializedName("org_country") var orgCountryId: String? = null,
    @SerializedName("org_postal_code") var orgPostalCode: String? = null,
    @SerializedName("is_gst") var isGst: Int? = null,
    @SerializedName("GST_number") var GSTNumber: String? = null,
    @SerializedName("image") var image: String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(contactNumber)
        parcel.writeString(countryCode)
        parcel.writeString(alternateNumber)
        parcel.writeString(email_id)
        parcel.writeString(gender)
        parcel.writeString(currency)
        parcel.writeString(addressLine1)
        parcel.writeString(addressLine2)
        parcel.writeString(cityId)
        parcel.writeString(stateId)
        parcel.writeString(countryId)
        parcel.writeString(postalCode)
        parcel.writeString(description)
        parcel.writeValue(isOrganization)
        parcel.writeString(companyName)
        parcel.writeString(orgContactNumber)
        parcel.writeString(officialEmail)
        parcel.writeString(companyWebsiteUrl)
        parcel.writeString(orgAddressLine1)
        parcel.writeString(orgAddressLine2)
        parcel.writeString(orgCityId)
        parcel.writeString(orgStateId)
        parcel.writeString(orgCountryId)
        parcel.writeString(orgPostalCode)
        parcel.writeValue(isGst)
        parcel.writeString(GSTNumber)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileData> {
        override fun createFromParcel(parcel: Parcel): ProfileData {
            return ProfileData(parcel)
        }

        override fun newArray(size: Int): Array<ProfileData?> {
            return arrayOfNulls(size)
        }
    }

    fun getImageCode(): String? {
        return image ?: code
    }
}