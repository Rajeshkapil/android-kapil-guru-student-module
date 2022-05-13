package com.kapilguru.student.exam.questionPaper.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kapilguru.student.exam.model.*
import com.kapilguru.student.exam.questionPaper.QuestionPaperRepository
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.toBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class QuestionPaperViewModel(private val repository: QuestionPaperRepository) : ViewModel() {
    //Object from intent
    lateinit var questionPaperInfo: QuestionPaperListItemResData
    // Question api response
    var questionsResponse: MutableLiveData<ApiResource<QuestionsReponse>> = MutableLiveData()
    // Question api response is stored here
    var questionsAndOptions: MutableLiveData<ArrayList<QuestionsAndOptions>> = MutableLiveData(ArrayList())
    // submit request - questionsList
    var submitAllQuestions: MutableLiveData<ArrayList<SubmitQuestionRequest>> = MutableLiveData(ArrayList())
    //current question Index
    var currentQuestionIndex: MutableLiveData<Int> = MutableLiveData()
    // Current question
    var currentQuestionAndOption: MutableLiveData<QuestionsAndOptions> = MutableLiveData()
    var isNextQuestionClicked = false
    var submitQuestionApiResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var submitAllQuestionsApiResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var isSubmitClicked = false

    fun getQuestionsRequest(request: QuestionsRequest) {
        questionsResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionsResponse.postValue(ApiResource.success(data = repository.getQuestions(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                questionsResponse.postValue(ApiResource.error(data = null, message = http.message,http.code))
            } catch (http: HttpException) {
                questionsResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                questionsResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    fun submitQuestionResponse(selectedOption: String?) {
        val request = getSubmitQuestionRequest(selectedOption)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                submitQuestionApiResponse.postValue(ApiResource.success(data = repository.submitQuestion(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = http.message,http.code))
            } catch (http: HttpException) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                submitQuestionApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    private fun getSubmitQuestionRequest(optionSelected: String?): SubmitQuestionRequest {
        return SubmitQuestionRequest(
            courseId = questionPaperInfo.courseId,
            questionPaperId = questionPaperInfo.questionPaperId,
            batchId = questionPaperInfo.batchId,
            isAttempted = 1,
            studentId = questionPaperInfo.studentId.toString(),
            questionId = currentQuestionAndOption.value?.questionId!!,
            selectedOpt = optionSelected,
            trainerId = questionPaperInfo.trainerId,
            examId = questionPaperInfo.examId
        )
    }

    fun submitAllQuestionsResponse() {
        submitAllQuestionsApiResponse.value = ApiResource.loading(data = null)
        val request = getSubmitAllQuestionsRequest()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                submitAllQuestionsApiResponse.postValue(ApiResource.success(data = repository.submitAllQuestions(request)))
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = http.message,http.code))
            } catch (http: HttpException) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                submitAllQuestionsApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    private fun getSubmitAllQuestionsRequest(): SubmitAllQuestionsRequest {
        val submitAllQuestionsList : ArrayList<SubmitQuestionRequest>? = submitAllQuestions.value
        val jsonArray1 = JSONArray()
        submitAllQuestionsList?.let { listNotNull ->
            for(question in listNotNull){
                val jsonObject = question.getJsonObject()
                jsonArray1.put(jsonObject)
            }
        }
        val encodedString = jsonArray1.toString().toBase64()
        return SubmitAllQuestionsRequest(encodedString)
    }
}