package com.kapilguru.student.myClassRoomDetails.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.myClassRoomDetails.MyClassRoomDetailsRepository

class MyClassDetailsFactory(private val apiHelper: ApiHelper, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyClassDetailsViewModel::class.java)) {
            return MyClassDetailsViewModel(
                MyClassRoomDetailsRepository(apiHelper), application
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}