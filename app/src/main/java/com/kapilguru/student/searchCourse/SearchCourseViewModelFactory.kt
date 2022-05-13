package com.kapilguru.student.searchCourse

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper

class SearchCourseViewModelFactory(private val apiHelper: ApiHelper, var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchCourseViewModel::class.java)) {
            return SearchCourseViewModel(
                SearchCourseRepository(apiHelper),application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}