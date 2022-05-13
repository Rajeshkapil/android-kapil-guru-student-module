package com.kapilguru.student.announcement.inbox.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import com.kapilguru.student.convertBase64ToString

data class InboxItem(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("subject") var subject: String? = "",
    @SerializedName("message") var message: String? = "",
    @SerializedName("announcement_type") var announcementType: String? = "",
    @SerializedName("sender_id") var senderId: Int? = null,
    @SerializedName("reciever_id") var recieverId: Int? = null,
    @SerializedName("created_by") var createdBy: Int? = null,
    @SerializedName("created_date") var createdDate: String = "",
    @SerializedName("modified_by") var modifiedBy: String? = "",
    @SerializedName("modified_date") var modifiedDate: String? = "",
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("sender_type") val senderType: String?= null,
    @SerializedName("reciever_type") val recieverType: String?= null,
    @SerializedName("sender_name") val senderName: String?= null,
    @SerializedName("sender_code") val senderCode: String?= null,
    @SerializedName("batch_code") val batchCode: String?= null,
    var previousTappedPosition: Int? = null,
    val shouldShow: MutableLiveData<Boolean> = MutableLiveData(false)
){
    val TAG = "InboxItem"
    var getBatchId = id.toString()
    var getCreatedTime = createdBy.toString()


    fun getDecodeSubject() : String{
        val data = String.convertBase64ToString(subject)
        return data
    }

    fun getDecodeMessage() : String{
        val data = String.convertBase64ToString(message)
        return data
    }
}