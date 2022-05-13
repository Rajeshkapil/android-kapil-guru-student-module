package com.kapilguru.student.announcement.sentItems.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.convertBase64ToString

data class SentItemsData(
    @SerializedName("id") var id: Int,
    @SerializedName("subject") var subject: String = "",
    @SerializedName("message") var message: String = "",
    @SerializedName("announcement_type") var announcementType: String,
    @SerializedName("sender_id") var senderId: Int,
    @SerializedName("reciever_id") var recieverId: Int,
    @SerializedName("created_by") var createdBy: Int,
    @SerializedName("created_date") var createdDate: String,
    @SerializedName("modified_by") var modifiedBy: Int,
    @SerializedName("modified_date") var modified_date: String,
    @SerializedName("is_active") var isActive: Int,
    @SerializedName("sender_type") val senderType: String,
    @SerializedName("reciever_type") val recieverType: String,
    @SerializedName("reciever_name") val recieverName: String,
    @SerializedName("reciever_code") val recieverCode: String,
    @SerializedName("batch_code") val batchCode: String,
    @SerializedName("batch_time") val batchTime: String,
    var previousTappedPosition: Int? = null,
    var shouldShow: MutableLiveData<Boolean> = MutableLiveData(false)
) {
    val TAG = "SentItemsData"

    fun getDecodeSubject(): String {
        val data = String.convertBase64ToString(subject)
        return data
    }

    fun getDecodeMessage(): String {
        val data = String.convertBase64ToString(message)
        return data
    }
}