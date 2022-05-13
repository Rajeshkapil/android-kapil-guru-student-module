package com.kapilguru.student.allTrendingWebinars.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository
import com.kapilguru.student.homeActivity.models.AllDemosResponse
import com.kapilguru.student.homeActivity.models.AllWebinarsResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AllTrendingWebinarsViewModel(val repository: HomeScreenRepository) : ViewModel() {
    private val TAG = "AllTrendingWebinarsViewModel"
    val allWebinarsData: MutableLiveData<ApiResource<AllWebinarsResponse>> = MutableLiveData()

    fun fetchAllWebinars() {
        val userId = StorePreferences(context).studentId
        allWebinarsData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allWebinarsData.postValue(
                    ApiResource.success(
                        repository.fetchAllWebinars()
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: IOException) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            } catch (exception: HttpException) {
                allWebinarsData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

}