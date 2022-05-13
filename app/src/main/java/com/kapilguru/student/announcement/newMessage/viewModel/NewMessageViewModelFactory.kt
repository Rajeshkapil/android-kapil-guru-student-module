package com.kapilguru.student.announcement.newMessage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.announcement.newMessage.viewModel.NewMessageViewModel
import com.kapilguru.student.announcement.AnnouncementRepository

class NewMessageViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewMessageViewModel::class.java)) {
            return NewMessageViewModel(
                AnnouncementRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}