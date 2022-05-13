package com.kapilguru.student.topCategories.selectedTopCategory.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.topCategories.selectedTopCategory.SelectedTopCategoryCourseRepository

class SelectedTopCategoryViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedTopCategoryViewModel::class.java)) {
            return SelectedTopCategoryViewModel(SelectedTopCategoryCourseRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}