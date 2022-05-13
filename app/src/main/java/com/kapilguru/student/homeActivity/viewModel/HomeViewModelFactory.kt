package com.kapilguru.student.homeActivity.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.HomeRepository

class HomeViewModelFactory(private val apiHelper: ApiHelper, private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(HomeRepository(apiHelper), app) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}