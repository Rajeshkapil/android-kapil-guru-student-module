package com.kapilguru.student.ui.changePassword

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.student.ui.changePassword.model.LogoutRequest

class ChangePasswordRepository(private val apiHelper : ApiHelper) {
    suspend fun changePassword(changePasswordRequest : ChangePasswordRequest) = apiHelper.changePassword(changePasswordRequest)
    suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)
}