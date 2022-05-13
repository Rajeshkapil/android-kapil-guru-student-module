package com.kapilguru.student.allTrendingDemos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.homeActivity.dashboard.HomeScreenRepository
import com.kapilguru.student.homeActivity.models.AllDemosResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AllTrendingDemosViewModel(val repository: HomeScreenRepository) : ViewModel() {
    private val TAG = "AllTrendingDemosViewModel"
    val alDemosData: MutableLiveData<ApiResource<AllDemosResponse>> = MutableLiveData()

    fun fetchAllDemos() {
        alDemosData.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alDemosData.postValue(ApiResource.success(repository.fetchAllDemos()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            } catch (exception: IOException) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                alDemosData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}