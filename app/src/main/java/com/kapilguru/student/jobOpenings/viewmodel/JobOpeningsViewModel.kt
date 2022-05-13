package com.kapilguru.student.jobOpenings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.demoLecture.DemoLectureRepository
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureDetailsResponse
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures
import com.kapilguru.student.demoLecture.model.DemoLectureResponseAPI
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.jobOpenings.JobOpeningsRepository
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.model.JobOpeningsResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class JobOpeningsViewModel(private val repository: JobOpeningsRepository, application: Application) :AndroidViewModel(application) {

    private val TAG = "MyClassroomViewModel"
    val userId = StorePreferences(application).studentId.toString()
    lateinit var trendingJobsList: ArrayList<JobOpeningsData>
    val allJobOpeningsData: MutableLiveData<ApiResource<JobOpeningsResponse>> = MutableLiveData()


    fun fetchAllJobOpenings() {
        allJobOpeningsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allJobOpeningsData.postValue(
                    ApiResource.success(
                        repository.fetchAllJobOpenings()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = NETWORK_CONNECTIVITY_EROR)
                )
            } catch (exception: IOException) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                allJobOpeningsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

}