package com.kapilguru.student.myClassRoomDetails

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListRequest
import com.kapilguru.student.myClassRoomDetails.model.RaiseComplaintRequest
import com.kapilguru.student.myClassRoomDetails.model.RefundRequest
import com.kapilguru.student.myClassRoomDetails.model.ReviewRequest


class MyClassRoomDetailsRepository(private val apiHelper : ApiHelper) {

    suspend fun getBatchDetails(batchId: String) = apiHelper.getBatchDetails(batchId)

    suspend fun raiseComplaint(request: RaiseComplaintRequest) = apiHelper.raiseComplaint(request)

    suspend fun refundRequest(request: RefundRequest) = apiHelper.refundRequest(request)

    suspend fun updateReview(reviewRequest: ReviewRequest) = apiHelper.updateReview(reviewRequest)

    suspend fun getExamList(questionPaperListRequest: QuestionPaperListRequest) = apiHelper.getExamList(questionPaperListRequest)

    suspend fun getStudyMaterialList(batchId: String) = apiHelper.getStudyMaterialList(batchId)
}