package com.kapilguru.student.completionRequest

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest

class CompletionRequestRepository(private val apiHelper: ApiHelper) {
    suspend fun getCompletionRequests(studentId: String) = apiHelper.getCompletionRequests(studentId)
    suspend fun updateCompletionReqStatus(studentId: String, updateCompletionRequest: UpdateCompletionRequest) = apiHelper.updateCompletionRequest(studentId, updateCompletionRequest)
}