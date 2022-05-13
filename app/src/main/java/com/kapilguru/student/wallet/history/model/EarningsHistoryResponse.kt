package com.kapilguru.student.wallet.history.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EarningsHistoryResponse(
    @SerializedName("created_by")
    val createdBy: Int? = 0,
    @SerializedName("created_date")
    var createdDate: String? = "",
    @SerializedName("deducted_amount")
    val deductedAmount: Int? = 0,
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("is_paid")
    val isPaid: Int? = 0,
    @SerializedName("is_processed")
    val isProcessed: Int? = 0,
    @SerializedName("paid_money_id")
    val paidMoneyId: Int? = 0,
    @SerializedName("payable_amount")
    val payableAmount: Double? = 0.0,
    @SerializedName("prizes_amount")
    val prizesAmount: Double? = 0.0,
    @SerializedName("referrals_amount")
    val referralsAmount: Double? = 0.0,
    @SerializedName("refunds_amount")
    val refundsAmount: Double? = 0.0,
    @SerializedName("remarks")
    val remarks: String? = null,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("total_amount")
    val totalAmount: Int? = 0,
    @SerializedName("user_id")
    val userId: Int? = 0,
    @SerializedName("user_name")
    val userName: String? = "",
    @Expose(serialize = false , deserialize = false)
    val shouldShow: MutableLiveData<Boolean> = MutableLiveData(false)
) {


}