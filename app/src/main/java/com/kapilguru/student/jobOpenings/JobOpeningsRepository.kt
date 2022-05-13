package com.kapilguru.student.jobOpenings

import com.kapilguru.student.ApiHelper

class JobOpeningsRepository(val apiHelper: ApiHelper) {
    suspend  fun fetchAllJobOpenings() = apiHelper.getAllJobOpenings()
}