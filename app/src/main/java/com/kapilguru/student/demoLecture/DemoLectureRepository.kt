package com.kapilguru.student.demoLecture

import com.kapilguru.student.ApiHelper

class DemoLectureRepository(val apiHelper: ApiHelper) {
    suspend fun getAllDemoLectures(userId: String) = apiHelper.getAllDemoLectures(userId)
    suspend fun getDemoLectureDetails(demoLectureId : String) = apiHelper.getDemoLectureDetails(demoLectureId)
}