package com.kapilguru.student.announcement.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.announcement.AnnouncementRepository

class AnnouncementViewModelFactory(private val apiHelper: ApiHelper,private  val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnnouncementViewModel::class.java)) {
            return AnnouncementViewModel(
                AnnouncementRepository(apiHelper),
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}