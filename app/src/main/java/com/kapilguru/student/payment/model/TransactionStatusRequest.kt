package com.kapilguru.student.payment.model

import com.google.gson.annotations.SerializedName

data class TransactionStatusRequest(
    @SerializedName("amount") var amount: Double? = 0.0,
    @SerializedName("product_type") var productType: String? = "",
    @SerializedName("orderId") var orderId: String? = "",
    @SerializedName("user_id") var userId: Int? = 0,
    @SerializedName("product_id") var productId: Int? = 0,
    @SerializedName("status") var status: String? = "",
    @SerializedName("trainer_id") var trainerId: Int? = 0,
    @SerializedName("course_id") var courseId: Int? = null)