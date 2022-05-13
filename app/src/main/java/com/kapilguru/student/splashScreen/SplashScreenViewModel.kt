package com.kapilguru.student.splashScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.ui.profile.ProfileOptionsRepository
import com.kapilguru.student.ui.profile.data.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SplashScreenViewModel(private val profileOptionsRepository: ProfileOptionsRepository) :
    ViewModel() {
    private val TAG: String = "ProfileOptionsViewModel"
    var profileDataResponse: MutableLiveData<ApiResource<ProfileResponse>> = MutableLiveData()


    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch {
            try {
                profileDataResponse.postValue(ApiResource.success(profileOptionsRepository.getProfileData(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                Log.d(TAG, "getProfileData: NetworkConnectionError")
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code= exception.code))
            } catch (exception: IOException) {
                Log.d(TAG, "getProfileData: IOException")
                profileDataResponse.postValue(ApiResource.error(code = 900, data = null, message = exception.message ?: "Error Occurred!"))
            }  catch (exception: HttpException) {
                Log.d(TAG, "getProfileData: HttpException")
                profileDataResponse.postValue(ApiResource.error(code = exception.code(), data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: Exception) {
                Log.d(TAG, "getProfileData: Exception")
                profileDataResponse.postValue(ApiResource.error(code = 500, data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}