package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class NotifyNewKeyWordRequest(
    @SerializedName("keyword")
    var keyword: String? = null
)