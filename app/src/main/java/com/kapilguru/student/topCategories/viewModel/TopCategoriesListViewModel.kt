package com.kapilguru.student.topCategories.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.homeActivity.models.TopCategoriesResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.topCategories.TopCategoriesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TopCategoriesListViewModel(private val repository : TopCategoriesListRepository) : ViewModel(){
    private val TAG = "TopCategoriesListViewModel"
    val topCategoriesApiRes: MutableLiveData<ApiResource<TopCategoriesResponse>> = MutableLiveData()

    fun fetchTopCategories() {
        topCategoriesApiRes.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                topCategoriesApiRes.postValue(ApiResource.success(repository.fetchTopCategories()))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                topCategoriesApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code= exception.code))
            } catch (exception: HttpException) {
                topCategoriesApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                topCategoriesApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}