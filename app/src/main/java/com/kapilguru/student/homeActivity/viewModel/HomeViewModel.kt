package com.kapilguru.student.homeActivity.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.homeActivity.HomeRepository
import com.kapilguru.student.homeActivity.models.NotificationCountResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.changePassword.model.LogoutRequest
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.RuntimeException

class HomeViewModel(private val homeRepository: HomeRepository, val context: Application) : AndroidViewModel(context) {
    val logoutResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    val notificationCountResponse: MutableLiveData<ApiResource<NotificationCountResponse>> = MutableLiveData()

    fun logoutUser(logoutRequest: LogoutRequest) {
        logoutResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logoutResponse.postValue(ApiResource.success(data = homeRepository.logoutUser(logoutRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code))
            } catch (exception: IOException) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
        fun notificationCountApiCall() {
            notificationCountResponse.value = ApiResource.loading(data = null)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    notificationCountResponse.postValue(ApiResource.success(data = homeRepository.getNotificationCount(StorePreferences(context).studentId.toString())))
                } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code))
                } catch (exception: IOException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (exception: HttpException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                } catch (exception: RuntimeException) {
                    notificationCountResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }
            }
        }
    }