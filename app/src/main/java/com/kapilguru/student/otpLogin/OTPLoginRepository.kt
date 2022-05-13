package com.kapilguru.student.otpLogin

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.student.otpLogin.model.OTPLoginRequest
import com.kapilguru.student.otpLogin.model.OTPLoginValidateRequest
import com.kapilguru.student.signup.model.validateMail.ValidateMailRequest

class OTPLoginRepository(private val apiHelper: ApiHelper) {
    suspend fun validateMail(validateMailReq : ValidateMailRequest) = apiHelper.validateMail(validateMailReq)
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun validateOTPLogin(otpLoginValidateReq : OTPLoginValidateRequest) = apiHelper.validateOTPLogin(otpLoginValidateReq)
    suspend fun requestOTP(otpLoginReq : OTPLoginValidateRequest) = apiHelper.requestOTP(otpLoginReq)
    suspend fun otpLogin(otpLoginRequest : OTPLoginRequest) = apiHelper.otpLogin(otpLoginRequest)
}