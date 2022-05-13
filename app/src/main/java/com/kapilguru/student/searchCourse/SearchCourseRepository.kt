package com.kapilguru.student.searchCourse

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.login.models.LoginUserRequest
import com.kapilguru.student.searchCourse.model.CreateSearchLeadRequest
import com.kapilguru.student.searchCourse.model.NotifyNewKeyWordRequest
import com.kapilguru.student.searchCourse.model.SelectedSearchCourseRequest

class SearchCourseRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers(loginUserRequest: LoginUserRequest) = apiHelper.getUsers(loginUserRequest)

    suspend fun searchCourse(text: String) = apiHelper.getAllSearchCourseName(text)

    suspend fun selectedSearchCourse(input: SelectedSearchCourseRequest) = apiHelper.selectedSearchCourse(input)


    suspend fun notifyNewKeyWord(keyWord: NotifyNewKeyWordRequest) = apiHelper.notifyNewKeyWord(keyWord)

    suspend fun createLeadRequest(createSearchLeadRequest: CreateSearchLeadRequest) = apiHelper.createLeadRequest(createSearchLeadRequest)
}