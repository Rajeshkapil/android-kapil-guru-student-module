package com.kapilguru.student.topCategories.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.topCategories.TopCategoriesListRepository

class TopCategoriesListViewModelFactory(private val apiHelper : ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopCategoriesListViewModel::class.java)) {
            return TopCategoriesListViewModel(TopCategoriesListRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}