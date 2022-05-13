package com.kapilguru.student.homeActivity.models

import com.google.gson.annotations.SerializedName

data class CreateLeadRequest(
    @SerializedName("email_id")
    var emailId: String? = "",
    @SerializedName("full_name")
    var fullName: String? = "",
    @SerializedName("source_id")
    var sourceId: Int ?= 51445,
    @SerializedName("source")
    var source: String? = "GET_IN_TOUCH",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("contact_number")
    var contactNumber: String? = ""
)