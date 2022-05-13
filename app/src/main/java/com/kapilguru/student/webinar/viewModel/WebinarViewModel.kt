package com.kapilguru.student.webinar.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.webinar.WebinarRepository
import com.kapilguru.student.webinar.model.ActiveWebinar
import com.kapilguru.student.webinar.model.OnGoingWebinar
import com.kapilguru.student.webinar.model.WebinarResponseAPI
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WebinarViewModel(private val repository: WebinarRepository, application: Application) :AndroidViewModel(application) {

    private val TAG = "MyClassroomViewModel"
    val userId = StorePreferences(application).studentId.toString()
    val webinarResponseAPIMutable: MutableLiveData<ApiResource<WebinarResponseAPI>> = MutableLiveData()
    val liveUpComingClasses: MutableLiveData<ArrayList<OnGoingWebinar>> = MutableLiveData()
    val activeWebinarList: MutableLiveData<ArrayList<ActiveWebinar>> = MutableLiveData()
    val webinarDetailAPiRes : MutableLiveData<ApiResource<WebinarDetailsResponse>> = MutableLiveData()
    val liveWebinars : MutableLiveData<List<OnGoingWebinar>> = MutableLiveData()
    val onGoingWebinars : MutableLiveData<List<OnGoingWebinar>> = MutableLiveData()

    fun getAllWebinars() {
        webinarResponseAPIMutable.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webinarResponseAPIMutable.postValue(ApiResource.success(data = repository.getAllClasses(userId)))
            } catch (exception: HttpException) {
                webinarResponseAPIMutable.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }catch (exception: IOException) {
                webinarResponseAPIMutable.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getWebinarDetails(webinarId : String){
        webinarDetailAPiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webinarDetailAPiRes.postValue(ApiResource.success(data = repository.getWebinarDetails(webinarId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                webinarDetailAPiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                webinarDetailAPiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (e: IOException) {
                webinarDetailAPiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }

}