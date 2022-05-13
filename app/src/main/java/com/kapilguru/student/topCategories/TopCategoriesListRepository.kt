package com.kapilguru.student.topCategories

import com.kapilguru.student.ApiHelper

class TopCategoriesListRepository(private val apiHelper : ApiHelper) {
    suspend  fun fetchTopCategories() = apiHelper.fetchTopCategories()
}