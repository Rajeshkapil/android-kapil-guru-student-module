package com.kapilguru.student.referandearn.myReferrals

import com.kapilguru.student.ApiHelper

class MyReferralRepository(private val apiHelper: ApiHelper) {
    suspend fun getMyReferrals(studentId : String) = apiHelper.getMyReferrals(studentId)
}