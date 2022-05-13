package com.kapilguru.student.announcement

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.announcement.inbox.data.LastMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendAdminMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendNewMessageRequest

class AnnouncementRepository(private val apiHelper: ApiHelper) {

    suspend fun getBatchList(userId: String) = apiHelper.getBatchList(userId)

    suspend fun getAdminList() = apiHelper.getAdminList()

    suspend fun sendNewMessageRequest(request : SendNewMessageRequest) = apiHelper.sendNewMessageReq(request)

    suspend fun sendAdminMessageRequest(request : SendAdminMessageRequest) = apiHelper.sendAdminNewMessageReq(request)

    suspend fun getInBoxResponse(userId : String) = apiHelper.getInBoxResponse(userId)

    suspend fun getSentItemsResponse(userId : String) = apiHelper.getSentItemsResponse(userId)

    suspend fun updateLastMessageId(userId : String,lastMessageRequest: LastMessageRequest) = apiHelper.updateLastMessageId(userId,lastMessageRequest)

}