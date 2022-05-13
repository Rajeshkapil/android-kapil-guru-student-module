package com.kapilguru.student.payment.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class InitiateTransactionResDataBody(
    @SerializedName("resultInfo") var resultInfo: ResultInfo? = null,
    @SerializedName("txnToken") var txnToken: String = "",
    @SerializedName("isPromoCodeValid") var isPromoCodeValid: Boolean = false,
    @SerializedName("authenticated") var authenticated: Boolean = false,
) {
    fun getObject(jsonObject: JSONObject): InitiateTransactionResDataBody {
        this.resultInfo = ResultInfo().getObject(jsonObject.getJSONObject("resultInfo"))
        this.txnToken = jsonObject.optString("txnToken")
        this.isPromoCodeValid = jsonObject.optBoolean("isPromoCodeValid")
        this.authenticated = jsonObject.optBoolean("authenticated")
        return this
    }
}