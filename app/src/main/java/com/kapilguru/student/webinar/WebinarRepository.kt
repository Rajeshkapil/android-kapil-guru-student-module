package com.kapilguru.student.webinar

import com.kapilguru.student.ApiHelper

class WebinarRepository(val apiHelper: ApiHelper) {
    suspend fun getAllClasses(userId: String) = apiHelper.getAllWebinars(userId)
    suspend fun getCourseLanguage() = apiHelper.getCourseLanguages()
    suspend fun getWebinarDetails(webinarId : String) = apiHelper.getWebinarDetails(webinarId)
}