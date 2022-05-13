package com.kapilguru.student.ui.profile.data

import com.google.gson.annotations.SerializedName

data class SaveProfileResponse(
    @SerializedName("data") val data: SaveProfileResData,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
