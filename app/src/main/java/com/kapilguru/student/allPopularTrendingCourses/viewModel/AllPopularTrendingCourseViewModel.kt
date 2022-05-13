package com.kapilguru.student.allPopularTrendingCourses.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AllPopularTrendingCourseViewModel(val repository: HomeScreenRepository) : ViewModel() {
    private val TAG = "AllPopularTrendingCourseViewModel"
    val popularAndTrendingResponse: MutableLiveData<ApiResource<PopularAndTrendingResponse>> = MutableLiveData()

    fun fetchAllPopularAndTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                popularAndTrendingResponse.postValue(
                    ApiResource.success(repository.getAllPopularAndTrendingCourses())
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = NETWORK_CONNECTIVITY_EROR)
                )
            } catch (exception: HttpException) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                popularAndTrendingResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }
}