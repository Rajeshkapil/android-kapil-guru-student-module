package com.kapilguru.student.completionRequest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.completionRequest.CompletionRequestRepository
import com.kapilguru.student.completionRequest.model.CompletionRequestResData
import com.kapilguru.student.completionRequest.model.CompletionRequestResponse
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest
import com.kapilguru.student.forgotPassword.model.ChangePasswordResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CompletionRequestViewModel(private val repository: CompletionRequestRepository) : ViewModel() {
    private val TAG = "CompletionRequestViewModel"
    val completionRequestApiRes: MutableLiveData<ApiResource<CompletionRequestResponse>> = MutableLiveData()
    val completionRequestList = ArrayList<CompletionRequestResData>()
    var updateCompletionReqStatusApiRes: MutableLiveData<ApiResource<ChangePasswordResponse>> = MutableLiveData()
    var rejectCompletionRequest  = CompletionRequestResData()

    fun getCompletionRequests(studentId: String) {
        completionRequestApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                completionRequestApiRes.postValue(ApiResource.success(data = repository.getCompletionRequests(studentId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                completionRequestApiRes.postValue(ApiResource.error(data = null, exception.message,exception.code))
            } catch (exception: HttpException) {
                completionRequestApiRes.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            } catch (exception: IOException) {
                completionRequestApiRes.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateCompletionReqStatus(studentId: String, updateCompletionRequest: UpdateCompletionRequest) {
        updateCompletionReqStatusApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateCompletionReqStatusApiRes.postValue(ApiResource.success(data = repository.updateCompletionReqStatus(studentId, updateCompletionRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                updateCompletionReqStatusApiRes.postValue(ApiResource.error(data = null, exception.message, exception.code))
            } catch (exception: HttpException) {
                updateCompletionReqStatusApiRes.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            } catch (exception: IOException) {
                updateCompletionReqStatusApiRes.postValue(ApiResource.error(data = null, exception.message ?: "Error Occurred!"))
            }

        }
    }
}