package com.kapilguru.student.allTrendingDemos.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository

class AllTrendingDemosViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllTrendingDemosViewModel::class.java)) {
            return AllTrendingDemosViewModel(HomeScreenRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}