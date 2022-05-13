package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class CourseSyllabusResponse(

    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    var data: List<SyllabusAttachmentData>
)

data class SyllabusAttachmentData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("trainer_id")
    var trainerId: Int,
    @SerializedName("course_id")
    var courseId: Int,
    @SerializedName("document_title")
    val documentTitle: String,
    @SerializedName("document_path")
    val documentPath: String,
    @SerializedName("originalname")
    val originalname: String,
    @SerializedName("mimetype")
    val mimetype: String,
    @SerializedName("size")
    var size: Int,
    @SerializedName("filename")
    val filename: String,
    @SerializedName("is_active")
    var isActive: Int,
    @SerializedName("created_date")
    val createdDate: String
)
