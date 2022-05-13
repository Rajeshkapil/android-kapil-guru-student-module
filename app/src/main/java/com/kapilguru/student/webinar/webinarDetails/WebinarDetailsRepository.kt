package com.kapilguru.student.webinar.webinarDetails

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusRequest

class WebinarDetailsRepository(val apiHelper: ApiHelper) {
    suspend fun getWebinarDetails(webinarId : String) = apiHelper.getWebinarDetails(webinarId)
    suspend fun getWebinarRegisterStatus(webinarRegisterStatusRequest: WebinarRegisterStatusRequest) = apiHelper.getWebinarRegisterStatus(webinarRegisterStatusRequest)
}