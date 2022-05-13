package com.kapilguru.student.ui.profile

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPRequest
import com.kapilguru.student.ui.profile.data.ProfileData

class ProfileOptionsRepository(private val apiHelper: ApiHelper) {
    suspend fun getProfileData(userId: String) = apiHelper.getProfileData(userId)
    suspend fun getOTRequest(getOTPRequest : GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
}