package com.kapilguru.student.login.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.BuildConfig
import com.kapilguru.student.emailValidation
import com.kapilguru.student.login.LoginRepository
import com.kapilguru.student.login.models.LoginResponse
import com.kapilguru.student.login.models.LoginUserRequest
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val TAG = "LoginViewModel"
    var showLoadingIndicator: MutableLiveData<Boolean> = MutableLiveData(false)
    var userName: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    var resultDat: MutableLiveData<ApiResource<LoginResponse>> = MutableLiveData()
    var errorOnValidations: MutableLiveData<InValidErrors> = MutableLiveData()

    init {
        if (!BuildConfig.IS_RELEASE_BUILD) {
//            userName.value = "suresh.gundaa@gmail.com"
//            password.value = "Sample@123"
//            userName.value = "manoharrathod111@gmail.com"
//            password.value = "Kapil@2021"
//            userName.value = "niharg@kapilit.com"
//            password.value = "Nihar@123"
//            userName.value = "rajeshk@kapilit.com" // beta
//            password.value = "Sample@123"
            userName.value = "ritwikpradhan@kapilit.com" //production
            password.value = "kapil@123"
//            userName.value = "manoharrathod111@gmail.com" // production
//            password.value = "Kapil@2021"

        }
    }

    fun onSubmitClick(v: View) {
        when {
            userName.value?.trim().isNullOrEmpty() -> {
                errorOnValidations.value = InValidErrors.EMAILINCORRECT
                return
            }

            userName.value?.trim()!!.emailValidation() -> {
                errorOnValidations.value = InValidErrors.EMAILINCORRECT
                return
            }

            password.value?.trim().isNullOrEmpty() -> {
                errorOnValidations.value = InValidErrors.PASSWORDINCORRECT
                return
            }
            else -> {
                val loginRequest = LoginUserRequest(userName.value.toString(), password.value.toString())
                loginUserApi(loginRequest)
            }
        }

    }

    fun loginUserApi(loginRequest: LoginUserRequest) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(loginRepository.getUsers(loginRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", exception.code()))
            }
        }
    }
}


// Dont delete this as it may be used for future references
//    fun loginUser (loginRequest: LoginUserRequest) = liveData(Dispatchers.IO) {
//        Log.v("checkInfo", "checkhere_2");
//        emit(ApiResource.loading(data = null))
//        try {
//            Log.v("checkInfo", "checkhere");
//            emit(ApiResource.success(data = loginRepository.getUsers(loginRequest)))
//        } catch (exception: Exception) {
//            Log.v("checkInfo", "checkhere_22");
//            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
//        }
//    }

enum class InValidErrors {
    PASSWORDINCORRECT,
    EMAILINCORRECT
}
