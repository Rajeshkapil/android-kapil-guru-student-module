package com.kapilguru.student.homeActivity.upcoming

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository

class UpcomingViewModelFactory(private val apiHelper: ApiHelper,
                               private val appliaction: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpComingViewModel::class.java)) {
            return UpComingViewModel(
                HomeScreenRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}