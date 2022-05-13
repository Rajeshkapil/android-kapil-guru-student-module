package com.kapilguru.student.ui.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.ui.profileInfo.models.CountryResponceItem

@Keep
data class CountryResponce(
    @SerializedName("data") val countryList: List<CountryResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
