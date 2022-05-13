package com.kapilguru.student.referandearn.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.courseDetails.model.EnrolledCourseResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.referandearn.ReferAndEarnRepository
import com.kapilguru.student.referandearn.model.ReferAndEarnRequest
import com.kapilguru.student.ui.profile.data.SaveProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.*

class ReferAndEarnViewModel(private val referAndEarnRepository: ReferAndEarnRepository, application: Application) : ViewModel() {

    var uuid: MutableLiveData<String> = MutableLiveData()
    var inviteeEmail: MutableLiveData<String> = MutableLiveData()
    var inviteeContactNumber: MutableLiveData<String> = MutableLiveData()
    var resultDat: MutableLiveData<ApiResource<SaveProfileResponse>> = MutableLiveData()
    var enrolledCourseResponse: MutableLiveData<ApiResource<EnrolledCourseResponse>> = MutableLiveData()
    var canRefer = false

    init {
        val generatedUUID: UUID = UUID.randomUUID()
        uuid.value = generatedUUID.toString()
    }

    fun requestApiCall(trainerId: Int, referralType: String) {
        val request = ReferAndEarnRequest().also { it ->
            it.referCode = uuid.value
            it.inviteeEmail = inviteeEmail.value
            it.inviteeContactNumber = inviteeContactNumber.value
            it.referralType = referralType
            it.userId = trainerId
        }
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(referAndEarnRepository.referAndEarn(request)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            }  catch (exception: HttpException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                resultDat.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }

        }
    }

    fun getEnrolledCourses(userId: Int) {
        enrolledCourseResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                enrolledCourseResponse.postValue(ApiResource.success(referAndEarnRepository.enrolledCourses(userId.toString())))
            } catch (exception: HttpException) {
                enrolledCourseResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                enrolledCourseResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }

        }
    }

}