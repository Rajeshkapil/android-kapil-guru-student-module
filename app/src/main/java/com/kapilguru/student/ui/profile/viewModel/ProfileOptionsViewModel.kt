package com.kapilguru.student.ui.profile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.ui.profile.ProfileOptionsRepository
import com.kapilguru.student.ui.profile.data.ProfileResponse
import com.kapilguru.student.ui.profile.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileOptionsViewModel(private val profileOptionsRepository: ProfileOptionsRepository) :
    ViewModel() {
    private val TAG: String = "ProfileOptionsViewModel"
    var profileDataResponse: MutableLiveData<ApiResource<ProfileResponse>> = MutableLiveData()
    var getOTPResponce : MutableLiveData<ApiResource<GetOTPResponce>> = MutableLiveData()
    var checkOTPResponce : MutableLiveData<ApiResource<CheckOTPResponce>> = MutableLiveData()
    var profilePercentage: MutableLiveData<Int> = MutableLiveData()
    var profileData: ProfileData = ProfileData()

    fun getOTP(getOTPRequest: GetOTPRequest){
        getOTPResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getOTPResponce.postValue(ApiResource.success(profileOptionsRepository.getOTRequest(getOTPRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                getOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileDataResponse.postValue(ApiResource.success(profileOptionsRepository.getProfileData(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",))
            } catch (exception:Exception) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",))
            }
        }
    }

    fun checkOTP(checkOTPRequest: CheckOTPRequest){
        checkOTPResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkOTPResponce.postValue(ApiResource.success(profileOptionsRepository.checkOTP(checkOTPRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: HttpException) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                checkOTPResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}