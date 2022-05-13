package com.kapilguru.student.referandearn.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.referandearn.ReferAndEarnRepository

class ReferAndEarnViewModelFactory(private val apiHelper: ApiHelper, private val appliaction: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReferAndEarnViewModel::class.java)) {
            return ReferAndEarnViewModel(ReferAndEarnRepository(apiHelper), appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}