package com.kapilguru.student.courseDetails.model

import com.google.gson.annotations.SerializedName

data class ContactTrainerResponse(
    @SerializedName("email_id")
    val emailId: String = "",
    @SerializedName("course_id")
    val courseId: Int = 0,
    @SerializedName("is_active")
    val isActive: Int = 0,
    @SerializedName("batch_id")
    val batchId: Int = 0,
    @SerializedName("student_id")
    val studentId: Int = 0,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("modified_date")
    val modifiedDate: String? = null,
    @SerializedName("created_by")
    val createdBy: Int = 0,
    @SerializedName("uuid")
    val uuid: String = "",
    @SerializedName("contact_number")
    val contactNumber: String = "",
    @SerializedName("is_added_by_trainer")
    val isAddedByTrainer: Int = 0,
    @SerializedName("country_code")
    val countryCode: Int = 0,
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("modified_by")
    val modifiedBy: String? = null,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("created_date")
    val createdDate: String = "",
    @SerializedName("is_other_country_code")
    val isOtherCountryCode: Int = 0,
    @SerializedName("trainer_id")
    val trainerId: Int = 0,
    @SerializedName("insertId")
    val insertId: Int = 0
)