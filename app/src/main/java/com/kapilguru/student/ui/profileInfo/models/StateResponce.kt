package com.kapilguru.student.ui.profileInfo.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.ui.profileInfo.models.StateResponceItem

@Keep
data class StateResponce(
    @SerializedName("data") val stateList: List<StateResponceItem>,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int)
