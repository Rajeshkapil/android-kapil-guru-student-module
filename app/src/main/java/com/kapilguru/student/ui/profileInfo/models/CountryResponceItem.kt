package com.kapilguru.student.ui.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CountryResponceItem(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("sortname") val sortName: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phonecode") val phoneCode: Int? = null,
    @SerializedName("is_active") val is_active: Int? = null
) {
    override fun toString(): String {
        return name!!
    }
}

