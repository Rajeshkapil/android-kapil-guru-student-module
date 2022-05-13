package com.kapilguru.student.allExamsList

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest

class AllExamsListRepository(private val apiHelper: ApiHelper) {

    suspend fun getAllExamsList(studentId: String) = apiHelper.getAllExamsList(studentId)
}