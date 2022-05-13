package com.kapilguru.student.myClassroom.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.newMessage.data.NewMessageData
import com.kapilguru.student.myClassroom.MyClassroomRepository
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData
import com.kapilguru.student.myClassroom.liveClasses.model.AllClassesResponse
import com.kapilguru.student.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyClassroomViewModel(private val repository: MyClassroomRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "MyClassroomViewModel"
    val userId = StorePreferences(application).studentId.toString()
    val allClassesResponse: MutableLiveData<ApiResource<AllClassesResponse>> =
        MutableLiveData()
    val liveUpComingClassDataList: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val liveClasses: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val upComingClasses: MutableLiveData<List<LiveUpComingClassData>> = MutableLiveData()
    val activeBatchesList: MutableLiveData<List<NewMessageData>> = MutableLiveData()
    val activeBatchesList1: ArrayList<ActiveBatchData> = ArrayList()

    fun getAllClasses() {
        allClassesResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allClassesResponse.postValue(ApiResource.success(data = repository.getAllClasses(userId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                allClassesResponse.postValue(ApiResource.error(data = null, message =  exception.message, code = exception.code))
            }catch (exception: IOException) {
                allClassesResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: HttpException) {
                allClassesResponse.postValue(ApiResource.error(data = null, exception.message() ?: "Error Occurred!"))
            }
        }
    }

    fun updateLiveUpComingClassList(updatedList: ArrayList<LiveUpComingClassData>) {
        liveUpComingClassDataList.value = null
        liveUpComingClassDataList.value = updatedList
        Log.d(TAG, "updateLiveUpComingClassList: " + liveClasses.value.toString())
    }

    fun updateActiveClasses() {

    }
}