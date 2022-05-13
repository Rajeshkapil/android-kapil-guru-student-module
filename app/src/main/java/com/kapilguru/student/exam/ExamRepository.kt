package com.kapilguru.student.exam

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.announcement.inbox.data.LastMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendAdminMessageRequest
import com.kapilguru.student.announcement.newMessage.data.SendNewMessageRequest
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest

class ExamRepository(private val apiHelper: ApiHelper) {

    suspend fun getStudentReport(studentReportRequest: StudentReportRequest) = apiHelper.getStudentReport(studentReportRequest)

    suspend fun getQuestions(questionsRequest: QuestionsRequest) = apiHelper.getQuestions(questionsRequest)

}