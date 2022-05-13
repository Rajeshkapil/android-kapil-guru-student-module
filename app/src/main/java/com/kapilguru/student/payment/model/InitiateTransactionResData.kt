package com.kapilguru.student.payment.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

// Below is the class for init Transaction and Transaction status API Response
data class InitiateTransactionResData(
    @SerializedName("head") var headJsonObject: JSONObject? = null,
    @SerializedName("body") var bodyJsonObject: JSONObject? = null,
    var head: InitiateTransactionResDataHead? = null,
    var body: InitiateTransactionResDataBody? = null,
) {

    fun getObject(initiateTransResData: String): InitiateTransactionResData {
        val jsonObject = JSONObject(initiateTransResData)
        if (jsonObject.has("head")) {
            this.head = InitiateTransactionResDataHead().getObject(jsonObject.getJSONObject("head"))
        }
        if (jsonObject.has("body")) {
            this.body = InitiateTransactionResDataBody().getObject(jsonObject.getJSONObject("body"))
        }
        return this
    }
}
