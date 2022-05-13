package com.kapilguru.student.network

import com.google.gson.annotations.SerializedName

data class CommonResponseData(
    @SerializedName("fieldCount") val fieldCount: Int = 0,
    @SerializedName("serverStatus") val serverStatus: Int = 0,
    @SerializedName("protocol41") val protocol: Boolean = false,
    @SerializedName("changedRows") val changedRows: Int = 0,
    @SerializedName("affectedRows") val affectedRows: Int = 0,
    @SerializedName("warningCount") val warningCount: Int = 0,
    @SerializedName("message") val message: String = "",
    @SerializedName("insertId") val insertId: Int = 0
)