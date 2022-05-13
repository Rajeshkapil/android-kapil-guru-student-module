package com.kapilguru.student.exam.questionPaper.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.exam.questionPaper.QuestionPaperRepository
import com.kapilguru.student.login.LoginRepository
import com.kapilguru.student.login.viewModel.LoginViewModel

class QuestionPaperViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionPaperViewModel::class.java)) {
            return QuestionPaperViewModel(
                QuestionPaperRepository(apiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}