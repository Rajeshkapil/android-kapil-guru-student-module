package com.kapilguru.student.ui.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.ui.profileInfo.models.CityResponceItem

@Keep
data class CityResponce(
    @SerializedName("data") val cityList: List<CityResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int
)

