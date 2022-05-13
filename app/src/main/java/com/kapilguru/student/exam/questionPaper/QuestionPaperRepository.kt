package com.kapilguru.student.exam.questionPaper

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.SubmitAllQuestionsRequest
import com.kapilguru.student.exam.model.SubmitQuestionRequest

class QuestionPaperRepository(private val apiHelper: ApiHelper) {
    suspend fun getQuestions(questionsRequest: QuestionsRequest) = apiHelper.getQuestions(questionsRequest)

    suspend fun submitQuestion(submitQuestionRequest: SubmitQuestionRequest) = apiHelper.submitQuestion(submitQuestionRequest)

    suspend fun submitAllQuestions(submitAllQuestionsRequest: SubmitAllQuestionsRequest) = apiHelper.submitAllQuestions(submitAllQuestionsRequest)

}