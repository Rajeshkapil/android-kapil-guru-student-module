package com.kapilguru.student.allPopularTrendingCourses.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository

class AllPopularTrendingCourseViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllPopularTrendingCourseViewModel::class.java)) {
            return AllPopularTrendingCourseViewModel(HomeScreenRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}