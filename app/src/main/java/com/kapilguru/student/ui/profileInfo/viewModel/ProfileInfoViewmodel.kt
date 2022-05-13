package com.kapilguru.student.ui.profileInfo.viewModel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.ui.profile.data.ProfileData
import com.kapilguru.student.ui.profile.data.ProfileResponse
import com.kapilguru.student.ui.profile.data.SaveProfileResponse
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourse
import com.kapilguru.student.ui.profileInfo.models.UploadImageCourseResponse
import com.kapilguru.student.ui.profileInfo.ProfileInfoRepository
import com.kapilguru.student.ui.profileInfo.models.CityResponce
import com.kapilguru.student.ui.profileInfo.models.CountryResponce
import com.kapilguru.student.ui.profileInfo.models.StateResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class ProfileInfoViewmodel(private val profileInfoRepository: ProfileInfoRepository) : ViewModel() {
    val uploadImageCourseResponse: MutableLiveData<ApiResource<UploadImageCourseResponse>> = MutableLiveData()
    private val TAG = "ProfileInfoViewmodel"
    val countryList: MutableLiveData<ApiResource<CountryResponce>> = MutableLiveData()
    val stateList: MutableLiveData<ApiResource<StateResponce>> = MutableLiveData()
    val cityList: MutableLiveData<ApiResource<CityResponce>> = MutableLiveData()
    val saveProfileResponse : MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()
    val profileDataResponse : MutableLiveData<ApiResource<ProfileResponse>> = MutableLiveData()
    var profileMutLiveData: MutableLiveData<ProfileData> = MutableLiveData()
    var errorDescription : MutableLiveData<String> = MutableLiveData()

    fun getCountryList() {
        countryList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                countryList.postValue(ApiResource.success(profileInfoRepository.getCountryList()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: HttpException) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                countryList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getStateList(countryId: Int) {
        stateList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                stateList.postValue(ApiResource.success(profileInfoRepository.getSateList(countryId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: HttpException) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                stateList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getCityList(stateId: Int) {
        cityList.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cityList.postValue(ApiResource.success(profileInfoRepository.getCityList(stateId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code)
                )
            } catch (exception: HttpException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                cityList.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }




    fun uploadProfileImage(uploadImageCourse: UploadImageCourse) {
        uploadImageCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                Log.d(TAG, "updatedText: ${uploadImageCourse.baseImage} other_ino_id${uploadImageCourse.sourceId}")
                uploadImageCourseResponse.postValue(
                    ApiResource.success(
                        profileInfoRepository.uploadCourseImage(uploadImageCourse)
                    )
                )
            } catch (e: HttpException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.code().toString() ?: "Error Occurred!")
                )
            }catch (e: IOException) {
                uploadImageCourseResponse.postValue(
                    ApiResource.error(data = null, message = e.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun dataValid() : Boolean{
        val profileData = profileMutLiveData.value
        if(TextUtils.isEmpty(profileData?.name)){
            errorDescription.postValue("Please enter name")
            return false
        }
        if(TextUtils.isEmpty(profileData?.contactNumber)){
            errorDescription.postValue("Please enter Contact Number")
            return false
        }
        if(TextUtils.isEmpty(profileData?.email_id)){
            errorDescription.postValue("Please enter Email")
            return false
        }
        if(TextUtils.isEmpty(profileData?.gender)){
            errorDescription.postValue("Please select Gender")
            return false
        }
       /* if(TextUtils.isEmpty(profileData?.currency)){
            errorDescription.postValue("Please select Currency")
            return false
        }*/
        if(TextUtils.isEmpty(profileData?.addressLine1)){
            errorDescription.postValue("Please enter Address")
            return false
        }
        if(TextUtils.isEmpty(profileData?.countryId)){
            errorDescription.postValue("Please select Country")
            return false
        }

        if(TextUtils.isEmpty(profileData?.stateId)){
            errorDescription.postValue("Please select State")
            return false
        }

        if(TextUtils.isEmpty(profileData?.cityId)){
            errorDescription.postValue("Please select State")
            return false
        }
        if(TextUtils.isEmpty(profileData?.postalCode)){
            errorDescription.postValue("Please enter Postal Code")
            return false
        }
        return true
    }

    fun dataOrganizationValid() : Boolean{
        val profileData = profileMutLiveData.value
        if(TextUtils.isEmpty(profileData?.orgContactNumber)){
            errorDescription.postValue("Please enter Contact Number")
            return false
        }
        if(TextUtils.isEmpty(profileData?.officialEmail)){
            errorDescription.postValue("Please enter Official Email")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgAddressLine1)){
            errorDescription.postValue("Please enter Address")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgCountryId)){
            errorDescription.postValue("Please select Country")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgStateId)){
            errorDescription.postValue("Please select State")
            return false
        }
        if(TextUtils.isEmpty(profileData?.orgCityId)){
            errorDescription.postValue("Please select City")
            return false
        }
        return true
    }

    fun getProfileData(userId: String) {
        profileDataResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileDataResponse.postValue(ApiResource.success(profileInfoRepository.getProfileData(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: HttpException) {
                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }



    fun updateProfileData() {
        saveProfileResponse.value = ApiResource.loading(null)
//        Log.d(TAG, "updateProfileData profile data: "+profileMutLiveData.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveProfileResponse.postValue(ApiResource.success(profileInfoRepository.updateProfile(
                    profileMutLiveData.value?.userId.toString(), profileMutLiveData.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: HttpException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                saveProfileResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}