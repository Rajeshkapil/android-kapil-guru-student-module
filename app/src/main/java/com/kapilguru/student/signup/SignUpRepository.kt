package com.kapilguru.student.signup

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.forgotPassword.model.ValidateMobileRequest
import com.kapilguru.student.signup.model.register.RegisterRequest
import com.kapilguru.student.signup.model.resendOtp.ResendOtpRequest
import com.kapilguru.student.signup.model.sendOtpSms.SendOtpSmsRequest
import com.kapilguru.student.signup.model.validateMail.ValidateMailRequest
import com.kapilguru.student.signup.model.validateOtp.ValidateOtpRequest

class SignUpRepository(private val apiHelper : ApiHelper) {
    suspend fun fetchCountryListForSignUp() = apiHelper.fetchCountryListForSignUp()
    suspend fun validateMail(validateMailReq : ValidateMailRequest) = apiHelper.validateMail(validateMailReq)
    suspend fun validateMobile(validateMobileRequest: ValidateMobileRequest) = apiHelper.validateMobile(validateMobileRequest)
    suspend fun validateOtp(validateotpReq : ValidateOtpRequest) = apiHelper.validateOtp(validateotpReq)
    suspend fun registerAccount(registerRequest : RegisterRequest) = apiHelper.registerAccount(registerRequest)
    suspend fun sendOtpMessage(sendOtpSmsRequest: SendOtpSmsRequest)= apiHelper.sendOtpMessage(sendOtpSmsRequest)
    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest)= apiHelper.resendOtp(resendOtpRequest)
}