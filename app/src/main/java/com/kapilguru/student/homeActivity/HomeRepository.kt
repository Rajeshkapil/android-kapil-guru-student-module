package com.kapilguru.student.homeActivity

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.ui.changePassword.model.LogoutRequest

class HomeRepository(private val apiHelper: ApiHelper) {

    suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)

    suspend fun getNotificationCount(userId : String) = apiHelper.getNotificationCount(userId)
}