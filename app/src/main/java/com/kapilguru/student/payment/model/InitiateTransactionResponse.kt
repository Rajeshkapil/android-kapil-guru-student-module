package com.kapilguru.student.payment.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class InitiateTransactionResponse(
    @SerializedName("first_response") var initiateTransResData: String = ""
){
}