package com.kapilguru.student.exam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.exam.model.*
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.exam.model.StudentResult
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ExamViewModel(val repository: ExamRepository,val app: Application): AndroidViewModel(app) {

    var studentReportResponseApi: MutableLiveData<ApiResource<StudentReportResponse>> = MutableLiveData()
    var studentResult: MutableLiveData<StudentResult> = MutableLiveData()

    // Question api response
    var questionsReponse: MutableLiveData<ApiResource<QuestionsReponse>> = MutableLiveData()
    // Question api response is stored here
    var questionsAndOptions: MutableLiveData<ArrayList<QuestionsAndOptions>> = MutableLiveData()
    // Question api response is stored
    var selectedOption: MutableLiveData<ArrayList<SelectedOption>> = MutableLiveData()

    // user selceted options are stored
    var selectedQuestionAndOption: MutableLiveData<QuestionsAndOptions> = MutableLiveData()

    
    
    fun getStudentReport(request: StudentReportRequest){
        studentReportResponseApi.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studentReportResponseApi.postValue(ApiResource.success(data = repository.getStudentReport(request))
                )
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = http.message,code = http.code))
            } catch (http: HttpException) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                studentReportResponseApi.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }

    fun getQuestionsRequest(request: QuestionsRequest){
        questionsReponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                questionsReponse.postValue(ApiResource.success(data = repository.getQuestions(request))
                )
            } catch (http: RetrofitNetwork.NetworkConnectionError) {
                questionsReponse.postValue(ApiResource.error(data = null, message = http.message,http.code))
            } catch (http: HttpException) {
                questionsReponse.postValue(ApiResource.error(data = null, message = http.message()))
            } catch (exception: IOException) {
                questionsReponse.postValue(ApiResource.error(data = null, message = exception.message ?: "try after some time"))
            }
        }
    }
    
}