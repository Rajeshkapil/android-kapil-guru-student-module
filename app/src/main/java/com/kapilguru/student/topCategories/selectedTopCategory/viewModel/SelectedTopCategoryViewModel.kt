package com.kapilguru.student.topCategories.selectedTopCategory.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.topCategories.selectedTopCategory.SelectedTopCategoryCourseRepository
import com.kapilguru.student.topCategories.selectedTopCategory.model.SelectedTopCategoryCourseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SelectedTopCategoryViewModel(private val repository : SelectedTopCategoryCourseRepository) : ViewModel(){
    private val TAG = "SelectedTopCategoryViewModel"
    var selectedCategory = TopCategoriesApi()
    val courseListApiRes : MutableLiveData<ApiResource<SelectedTopCategoryCourseResponse>> = MutableLiveData()

    fun getCourseByCategory(){
        courseListApiRes.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                courseListApiRes.postValue(ApiResource.success(repository.getCoursesByCategory(selectedCategory.id.toString())))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                courseListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                courseListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                courseListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}