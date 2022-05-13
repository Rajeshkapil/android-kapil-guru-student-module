package com.kapilguru.student.myClassRoomDetails.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialResponseApi(
    @SerializedName("document_title")
    val documentTitle: String? = "",
    @SerializedName("course_id")
    val courseId: Int? = 0,
    @SerializedName("is_active")
    val isActive: Int? = 0,
    @SerializedName("batch_id")
    val batchId: Int? = 0,
    @SerializedName("originalname")
    val originalname: String? = "",
    @SerializedName("modified_date")
    val modifiedDate: String? = null,
    @SerializedName("created_by")
    val createdBy: String? = null,
    @SerializedName("filename")
    val filename: String? = "",
    @SerializedName("size")
    val size: Int? = 0,
    @SerializedName("document_path")
    val documentPath: String? = "",
    @SerializedName("modified_by")
    val modifiedBy: String? = null,
    @SerializedName("mimetype")
    val mimetype: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("created_date")
    val createdDate: String? = "",
    @SerializedName("trainer_id")
    val trainerId: Int? = 0
)