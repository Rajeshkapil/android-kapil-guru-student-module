package com.kapilguru.student.searchCourse.model

import com.google.gson.annotations.SerializedName

data class NotifyNewKeyWordResponseApi(
    @SerializedName("notes")
    val notes: String? = null,
    @SerializedName("is_active")
    val isActive: Int? = 0,
    @SerializedName("modified_by")
    val modifiedBy: String? = null,
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("created_date")
    val createdDate: String? = "",
    @SerializedName("keyword")
    val keyword: String? = "",
    @SerializedName("modified_date")
    val modifiedDate: String? = null,
    @SerializedName("created_by")
    val createdBy: Int? = 0,
    @SerializedName("status")
    val status: Int? = 0,
    @SerializedName("insertId")
    val insertId: Int? = 0
)