package com.kapilguru.student.demoLecture.demoLectureDetails.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsRepository
import com.kapilguru.student.demoLecture.demoLectureDetails.model.*
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DemoLectureDetailsViewModel(private val repository: DemoLectureDetailsRepository) : ViewModel() {
    private val TAG = "DemoLectureDetailsViewModel"
    var demoLectureIntent: Int? = -1 // demo lecture data from intent
    var demoLectureDetailsApi: MutableLiveData<DemoLectureDetailsResData> = MutableLiveData(DemoLectureDetailsResData()) // demo lecture data from API
    val demoLectureDetailsApiRes: MutableLiveData<ApiResource<DemoLectureDetailsResponse>> = MutableLiveData()
    val demoLectureRegisterRequest : MutableLiveData<DemoLectureRegisterRequest> = MutableLiveData()
    val demoLectureRegisterApiResponse : MutableLiveData<ApiResource<DemoLectureRegisterResponse>> = MutableLiveData()
    val demoLectureRegisterStatusApiRes: MutableLiveData<ApiResource<DemoLectureRegisterStatusResponse>> = MutableLiveData()

    fun getDemoLectureDetails() {
        demoLectureDetailsApiRes.value = ApiResource.loading(data = null)
        if (demoLectureIntent != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    demoLectureDetailsApiRes.postValue(ApiResource.success(data = repository.getDemoLectureDetails(demoLectureIntent.toString())))
                } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                    demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
                } catch (exception: HttpException) {
                    demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (e: IOException) {
                    demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
                }
            }
        }
    }

    fun fetchDemoLectureRegisterStatus(demoLectureRegisterStatus : DemoLectureRegisterStatusRequest) {
        demoLectureRegisterStatusApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoLectureRegisterStatusApiRes.postValue(ApiResource.success(data = repository.checkDemoLectureRegisterStatus(demoLectureRegisterStatus)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                demoLectureRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                demoLectureRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (e: IOException) {
                demoLectureRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

    fun registerForDemoLecture(demoLectureRegisterRequest : DemoLectureRegisterRequest) {
        demoLectureRegisterApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoLectureRegisterApiResponse.postValue(ApiResource.success(data = repository.registerDemoLecture(demoLectureRegisterRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                demoLectureRegisterApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                demoLectureRegisterApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (e: IOException) {
                demoLectureRegisterApiResponse.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }
}