package com.kapilguru.student.allExamsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.completionRequest.CompletionRequestRepository

class AllExamsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllExamsViewModel::class.java)) {
            return AllExamsViewModel(AllExamsListRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}