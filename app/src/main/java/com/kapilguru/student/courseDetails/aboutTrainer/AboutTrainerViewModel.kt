package com.kapilguru.student.courseDetails.aboutTrainer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.courseDetails.CourseDetailsRepository
import com.kapilguru.student.courseDetails.model.*
import com.kapilguru.student.courseDetails.review.model.StudentReviewResponse
import com.kapilguru.student.courseDetails.review.model.WriteReviewRequest
import com.kapilguru.student.network.ApiResource
import kotlinx.coroutines.*
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AboutTrainerViewModel(private val courseDetailsRepository: CourseDetailsRepository) : ViewModel() {


    private val TAG = "AboutTrainerViewModel";


    var syllabusResponse: MutableLiveData<ApiResource<CourseSyllabusResponse>> = MutableLiveData()
    var contactTrainerResponseAPi: MutableLiveData<ApiResource<ContactTrainerResponseAPi>> = MutableLiveData()


    fun contactTrainer(contactTrainerRequest: ContactTrainerRequest) {
        contactTrainerResponseAPi.value = ApiResource.loading(data = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                contactTrainerResponseAPi.postValue(ApiResource.success(courseDetailsRepository.contactTrainer(contactTrainerRequest)))

            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code)
                )
            } catch (exception: IOException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                contactTrainerResponseAPi.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}

