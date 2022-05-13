package com.kapilguru.student.courseDetails.aboutTrainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.courseDetails.CourseDetailsRepository

class AboutTrainerViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AboutTrainerViewModel::class.java)) {
            return AboutTrainerViewModel(
                CourseDetailsRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}