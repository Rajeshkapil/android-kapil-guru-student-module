package com.kapilguru.student.jobOpenings.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.demoLecture.DemoLectureRepository
import com.kapilguru.student.jobOpenings.JobOpeningsRepository

class JobOpeningsViewModelFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobOpeningsViewModel::class.java)) {
            return JobOpeningsViewModel(
                JobOpeningsRepository(
                    apiHelper
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}