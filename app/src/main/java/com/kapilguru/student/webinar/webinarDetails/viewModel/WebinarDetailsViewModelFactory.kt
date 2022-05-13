package com.kapilguru.student.webinar.webinarDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsRepository

class WebinarDetailsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebinarDetailsViewModel::class.java)) {
            return WebinarDetailsViewModel(WebinarDetailsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}