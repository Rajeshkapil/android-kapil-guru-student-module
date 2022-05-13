package com.kapilguru.student.myClassRoomDetails.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RefundRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("trainer_id") var trainerId: Int,
    @SerializedName("course_id") var courseId: Int,
    @SerializedName("batch_id") var batchId: Int,
    @SerializedName("reason") val reason: String
)
