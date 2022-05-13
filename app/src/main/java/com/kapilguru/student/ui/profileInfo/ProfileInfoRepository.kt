package com.kapilguru.student.ui.profileInfo

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPRequest
import com.kapilguru.student.ui.profile.data.ProfileData
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourse


class ProfileInfoRepository(private val apiHelper: ApiHelper) {
    suspend fun getCountryList() = apiHelper.getCountryList()
    suspend fun getSateList(countryId: Int) = apiHelper.getStateList(countryId)
    suspend fun getCityList(stateId: Int) = apiHelper.getCityList(stateId)
    suspend fun getProfileData(userId: String) = apiHelper.getProfileData(userId)
    suspend fun updateProfile(userId: String, profileData: ProfileData) = apiHelper.updateProfileData(userId, profileData)
    suspend fun getOTRequest(getOTPRequest : GetOTPRequest) = apiHelper.generateOTP(getOTPRequest)
    suspend fun checkOTP(checkOTPRequest: CheckOTPRequest) = apiHelper.checkOTP(checkOTPRequest)
    suspend fun uploadCourseImage(uploadImageCourse: UploadImageCourse) = apiHelper.uploadCourseImage(uploadImageCourse)
}