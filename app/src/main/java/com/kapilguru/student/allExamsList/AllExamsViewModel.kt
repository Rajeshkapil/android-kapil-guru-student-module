package com.kapilguru.student.allExamsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.completionRequest.CompletionRequestRepository
import com.kapilguru.student.completionRequest.model.CompletionRequestResData
import com.kapilguru.student.completionRequest.model.CompletionRequestResponse
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest
import com.kapilguru.student.forgotPassword.model.ChangePasswordResponse
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AllExamsViewModel(private val repository: AllExamsListRepository) : ViewModel() {
    private val TAG = "CompletionRequestViewModel"
    val examsListResponseAPi: MutableLiveData<ApiResource<QuestionPaperListResponse>> = MutableLiveData()


    fun getCompletionRequests(studentId: String) {
        examsListResponseAPi.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                examsListResponseAPi.postValue(ApiResource.success(data = repository.getAllExamsList(studentId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!", exception.code))
            } catch (exception: HttpException) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            } catch (exception: IOException) {
                examsListResponseAPi.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }


}