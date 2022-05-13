package com.kapilguru.student.demoLecture.demoLectureDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsRepository

class DemoLectureDetailsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoLectureDetailsViewModel::class.java)) {
            return DemoLectureDetailsViewModel(DemoLectureDetailsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}