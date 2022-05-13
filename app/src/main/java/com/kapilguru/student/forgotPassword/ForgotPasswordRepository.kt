package com.kapilguru.student.forgotPassword

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.student.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPRequest

class ForgotPasswordRepository(private val apiHelper: ApiHelper) {
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun getOTP(otpRequest : GetOTPRequest) = apiHelper.generateOTP(otpRequest)
    suspend fun checkOTP(checkOTPReq : CheckOTPRequest) = apiHelper.checkOTP(checkOTPReq)
    suspend fun changePassword(changePasswordReq : ChangePasswordRequest) = apiHelper.changePassword(changePasswordReq)

}