package com.kapilguru.student.signup.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

//remove the class after profile integration
@Keep
data class CountryResponce(
    @SerializedName("data") val countryList: List<CountryResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
