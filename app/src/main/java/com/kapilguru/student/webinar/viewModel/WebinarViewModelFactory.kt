package com.kapilguru.student.webinar.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.webinar.WebinarRepository

class WebinarViewModelFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WebinarViewModel::class.java)) {
            return WebinarViewModel(
                WebinarRepository(
                    apiHelper
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}