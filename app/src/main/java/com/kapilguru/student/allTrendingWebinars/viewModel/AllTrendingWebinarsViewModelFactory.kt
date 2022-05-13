package com.kapilguru.student.allTrendingWebinars.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository

class AllTrendingWebinarsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllTrendingWebinarsViewModel::class.java)) {
            return AllTrendingWebinarsViewModel(HomeScreenRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}