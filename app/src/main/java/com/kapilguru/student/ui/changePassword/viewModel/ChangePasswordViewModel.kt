package com.kapilguru.student.ui.changePassword.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.ui.changePassword.model.LogoutRequest
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.forgotPassword.model.ChangePasswordRequest
import com.kapilguru.student.forgotPassword.model.ChangePasswordResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.changePassword.ChangePasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ChangePasswordViewModel(private val repository : ChangePasswordRepository, application : Application) : AndroidViewModel(application) {
    private val TAG = "ChangePasswordViewModel"
    val changePasswordRequest : MutableLiveData<ChangePasswordRequest> = MutableLiveData(
        ChangePasswordRequest()
    )
    val changePasswordResponse : MutableLiveData<ApiResource<ChangePasswordResponse>> = MutableLiveData()
    val logoutRequest : MutableLiveData<LogoutRequest> = MutableLiveData()
    val logoutResponse :MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    init {
        logoutRequest.value = LogoutRequest(StorePreferences(application).studentId,StorePreferences(application).trainerToken)
    }

    fun changePassword(){
        changePasswordResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try{
                changePasswordRequest.value?.let{changePasswordReq ->
                    changePasswordResponse.postValue(ApiResource.success(data=repository.changePassword(changePasswordReq)))
                }
            }catch (exception : HttpException){
                changePasswordResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception : IOException){
                changePasswordResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun logoutUser(){
        logoutResponse.value = ApiResource.loading(data = null)
        try{
            viewModelScope.launch(Dispatchers.IO) {
                logoutRequest.value?.let{ logoutReq ->
                    logoutResponse.postValue(ApiResource.success(data = repository.logoutUser(logoutReq)))
                }
            }
        }catch(exception : RetrofitNetwork.NetworkConnectionError){
            logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch(exception : IOException){
            logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }catch(exception : HttpException){
            logoutResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}