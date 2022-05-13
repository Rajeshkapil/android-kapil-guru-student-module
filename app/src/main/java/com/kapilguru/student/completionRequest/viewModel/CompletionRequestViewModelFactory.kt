package com.kapilguru.student.completionRequest.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.completionRequest.CompletionRequestRepository

class CompletionRequestViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletionRequestViewModel::class.java)) {
            return CompletionRequestViewModel(CompletionRequestRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}