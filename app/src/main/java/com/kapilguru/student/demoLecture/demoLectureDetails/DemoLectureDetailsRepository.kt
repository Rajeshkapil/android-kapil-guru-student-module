package com.kapilguru.student.demoLecture.demoLectureDetails

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterStatusRequest

class DemoLectureDetailsRepository(private val apiHelper: ApiHelper) {
    suspend fun getDemoLectureDetails(demoLectureId : String) = apiHelper.getDemoLectureDetails(demoLectureId)
    suspend fun registerDemoLecture(demoLectureRegisterRequest: DemoLectureRegisterRequest) = apiHelper.registerDemoLecture(demoLectureRegisterRequest)
    suspend fun checkDemoLectureRegisterStatus(demoLectureRegisterStatusRequest: DemoLectureRegisterStatusRequest) = apiHelper.checkDemoLectureRegisterStatus(demoLectureRegisterStatusRequest)
}