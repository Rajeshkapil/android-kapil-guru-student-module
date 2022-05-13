package com.kapilguru.student.homeActivity.dashboard

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingResponse


class HomeScreenRepository(private val apiHelper: ApiHelper) {

     suspend  fun fetchUpcomingSchedule(userId: String) = apiHelper.fetchUpcomingSchedule(userId)


     suspend  fun fetchTopCategories() = apiHelper.fetchTopCategories()


     suspend  fun fetchAllWebinars() = apiHelper.fetchAllWebinars()


     suspend  fun fetchAllDemos() = apiHelper.fetchAllDemos()

    suspend  fun fetchAllJobOpenings() = apiHelper.getAllJobOpenings()

     suspend fun getDashBoardPopularAndTrendingCourses() = apiHelper.getDashBoardPopularAndTrendingCourses()

     suspend fun getAllPopularAndTrendingCourses() = apiHelper.getAllPopularAndTrendingCourses()

    suspend  fun createLeadApi(createLeadRequest: CreateLeadRequest) = apiHelper.createLeadApi(createLeadRequest)


//     suspend fun logoutUser(logoutRequest : LogoutRequest) = apiHelper.logoutUser(logoutRequest)

}