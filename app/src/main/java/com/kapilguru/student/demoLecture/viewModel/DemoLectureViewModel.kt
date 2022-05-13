package com.kapilguru.student.demoLecture.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.demoLecture.DemoLectureRepository
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureDetailsResponse
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures
import com.kapilguru.student.demoLecture.model.DemoLectureResponseAPI
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DemoLectureViewModel(private val repository: DemoLectureRepository, application: Application) :AndroidViewModel(application) {

    private val TAG = "MyClassroomViewModel"
    val userId = StorePreferences(application).studentId.toString()
    val demoLectureResponseAPIMutable: MutableLiveData<ApiResource<DemoLectureResponseAPI>> = MutableLiveData()
    val liveOnGoingDemoLectures: MutableLiveData<ArrayList<OnGoingDemoLectures>> = MutableLiveData()
    val activeDemoLectures: MutableLiveData<ArrayList<ActiveDemoLectures>> = MutableLiveData()
    val demoLectureDetailsApiRes : MutableLiveData<ApiResource<DemoLectureDetailsResponse>> = MutableLiveData()
    val liveDemoLectures :MutableLiveData<List<OnGoingDemoLectures>> = MutableLiveData()
    val onGoingDemoLectures :MutableLiveData<List<OnGoingDemoLectures>> = MutableLiveData()


    fun getAllWebinars() {
        demoLectureResponseAPIMutable.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoLectureResponseAPIMutable.postValue(ApiResource.success(data = repository.getAllDemoLectures(userId)))
            } catch (exception: HttpException) {
                demoLectureResponseAPIMutable.postValue(
                    ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }catch (exception: IOException) {
                demoLectureResponseAPIMutable.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun getDemoLectureDetails(demoLectureId : String){
        demoLectureDetailsApiRes.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                demoLectureDetailsApiRes.postValue(ApiResource.success(data = repository.getDemoLectureDetails(demoLectureId)))
            } catch (exception: HttpException) {
                demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (e: IOException) {
                demoLectureDetailsApiRes.postValue(ApiResource.error(data = null, message = e.message ?: "Error Occurred!"))
            }
        }
    }
}