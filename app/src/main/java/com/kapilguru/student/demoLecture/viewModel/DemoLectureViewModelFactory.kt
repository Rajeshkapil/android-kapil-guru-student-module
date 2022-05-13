package com.kapilguru.student.demoLecture.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.demoLecture.DemoLectureRepository

class DemoLectureViewModelFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoLectureViewModel::class.java)) {
            return DemoLectureViewModel(
                DemoLectureRepository(
                    apiHelper
                ), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}