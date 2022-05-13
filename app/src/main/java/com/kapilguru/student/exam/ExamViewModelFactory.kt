package com.kapilguru.student.exam

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.announcement.viewModel.AnnouncementViewModel

class ExamViewModelFactory(private val apiHelper: ApiHelper, private  val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamViewModel::class.java)) {
            return ExamViewModel(
                ExamRepository(apiHelper),
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}