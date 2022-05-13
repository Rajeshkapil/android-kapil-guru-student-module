package com.kapilguru.student.courseDetails

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.courseDetails.model.BatchRequest
import com.kapilguru.student.courseDetails.model.ContactTrainerRequest
import com.kapilguru.student.courseDetails.review.model.WriteReviewRequest
import com.kapilguru.student.login.models.LoginUserRequest

class CourseDetailsRepository(private val apiHelper: ApiHelper) {

    suspend fun fetchCourseDetails(courseId: String) = apiHelper.fetchCourseDetails(courseId)

    suspend fun fetchSyllabusAttachments(syllabusAttachmentId: String) = apiHelper.fetchSyllabusAttachments(syllabusAttachmentId)

    suspend fun isCourseEnrolled(courseId: String) = apiHelper.isCourseEnrolled(courseId)

    suspend fun requestBatch(batchRequest: BatchRequest) =  apiHelper.requestBatch(batchRequest)

    suspend fun getStudentReviews(studentId: String) = apiHelper.getStudentReviews(studentId)

    suspend fun updateStudentReview(writeReviewRequest: WriteReviewRequest) = apiHelper.updateStudentReview(writeReviewRequest)

    suspend fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) = apiHelper.contactTrainer(contactTrainerRequest)

}