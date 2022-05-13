package com.kapilguru.student.referandearn

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.referandearn.model.ReferAndEarnRequest

class ReferAndEarnRepository(private val apiHelper: ApiHelper) {
    suspend fun referAndEarn(referAndEarnRequest: ReferAndEarnRequest) = apiHelper.postReferAndEarn(referAndEarnRequest)
    suspend fun enrolledCourses(userId: String) = apiHelper.isCourseEnrolled(userId)
}