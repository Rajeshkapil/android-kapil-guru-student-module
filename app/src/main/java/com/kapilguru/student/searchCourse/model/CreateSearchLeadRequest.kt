package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class CreateSearchLeadRequest(
    @SerializedName("email_id")
    var emailId: String? = "",
    @SerializedName("full_name")
    var fullName: String? = "",
    @SerializedName("source_id")
    val sourceId: Int? = 53338, // always constant
    @SerializedName("source")
    var source: String? = "SEARCH_PAGE",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("contact_number")
    var contactNumber: String? = ""
)