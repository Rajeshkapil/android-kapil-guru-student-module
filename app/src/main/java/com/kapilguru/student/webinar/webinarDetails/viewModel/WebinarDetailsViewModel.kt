package com.kapilguru.student.webinar.webinarDetails.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.webinar.model.ActiveWebinar
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsRepository
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResData
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResponse
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusRequest
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WebinarDetailsViewModel(private val repository: WebinarDetailsRepository) : ViewModel() {
    private val TAG = "WebinarDetailsViewModel"
     var webinarDataIntent :Int?= -1 // webinar Received from intent
    var webinarDetailsApi : MutableLiveData<WebinarDetailsResData> = MutableLiveData(WebinarDetailsResData()) // webinar Details from api call
    val webinarDetailsRes: MutableLiveData<ApiResource<WebinarDetailsResponse>> = MutableLiveData()
    val webinarRegisterStatusApiRes : MutableLiveData<ApiResource<WebinarRegisterStatusResponse>> = MutableLiveData()

    fun getWebinarDetailsResponse() {
        if (webinarDataIntent != -1) {
            webinarDetailsRes.value = ApiResource.loading(data = null)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    webinarDetailsRes.postValue(ApiResource.success(data = repository.getWebinarDetails(webinarDataIntent.toString())))
                } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                    webinarDetailsRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
                }catch (exception: HttpException) {
                    webinarDetailsRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (e: IOException) {
                    webinarDetailsRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
                }
            }
        }
    }

    fun getWebinarRegisterStatus(webinarRegisterStatusRequest : WebinarRegisterStatusRequest){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webinarRegisterStatusApiRes.postValue(ApiResource.success(data = repository.getWebinarRegisterStatus(webinarRegisterStatusRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                webinarRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            }catch (exception: HttpException) {
                webinarRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (e: IOException) {
                webinarRegisterStatusApiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }
}