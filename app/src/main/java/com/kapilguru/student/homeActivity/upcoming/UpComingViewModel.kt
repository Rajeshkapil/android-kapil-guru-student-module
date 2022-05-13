package com.kapilguru.student.homeActivity.upcoming

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.R
import com.kapilguru.student.emailValidation
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository
import com.kapilguru.student.homeActivity.models.*
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingApi
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingResponse
import com.kapilguru.student.isValidMobileNo
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.model.JobOpeningsResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.home.UpComingScheduleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import retrofit2.HttpException
import java.io.IOException

class UpComingViewModel(val homeScreenRepository: HomeScreenRepository, val context: Application,) : AndroidViewModel(context) {



    val upcomingResponse: MutableLiveData<ApiResource<UpComingScheduleResponse>> = MutableLiveData()


    fun fetchUpcomingSchedule() {
        val userId = StorePreferences(context).studentId
        upcomingResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                upcomingResponse.postValue(
                    ApiResource.success(
                        homeScreenRepository.fetchUpcomingSchedule(userId = userId.toString())
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: HttpException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: IOException) {
                upcomingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }




























}