package com.kapilguru.student.topCategories.selectedTopCategory

import com.kapilguru.student.ApiHelper

class SelectedTopCategoryCourseRepository(private val apiHelper: ApiHelper) {
    suspend fun getCoursesByCategory(categoryId : String) = apiHelper.getCourseByCategory(categoryId)
}