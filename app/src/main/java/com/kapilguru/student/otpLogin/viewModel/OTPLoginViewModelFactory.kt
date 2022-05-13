package com.kapilguru.student.otpLogin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.otpLogin.OTPLoginRepository

class OTPLoginViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OTPLoginViewModel::class.java)) {
            return OTPLoginViewModel(OTPLoginRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}