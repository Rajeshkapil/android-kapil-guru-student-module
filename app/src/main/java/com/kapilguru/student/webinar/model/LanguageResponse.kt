package com.kapilguru.student.webinar.model

import com.google.gson.annotations.SerializedName

data class LanguageResponse(
    @SerializedName("status") val status : Int,
    @SerializedName("message") val message : String,
    @SerializedName("data") val languageData : ArrayList<LanguageData>
)
