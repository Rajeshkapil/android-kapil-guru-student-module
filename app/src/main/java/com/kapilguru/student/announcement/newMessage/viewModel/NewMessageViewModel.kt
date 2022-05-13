package com.kapilguru.student.announcement.newMessage.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.AnnouncementRepository
import com.kapilguru.student.announcement.newMessage.data.SendNewMessageRequest
import com.kapilguru.student.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.student.announcement.newMessage.data.SendNewMessageResponce
import com.kapilguru.student.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class NewMessageViewModel(private val announcementRepository: AnnouncementRepository) :
    ViewModel() {
    val TAG = "NewMessageViewModel"
    var batchListMutLiveData: MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    var sendNewMessageResponce: MutableLiveData<ApiResource<SendNewMessageResponce>> =
        MutableLiveData()
    var subjectMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var messageMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var selectedBatchId: MutableLiveData<String> = MutableLiveData("")


    fun getBatchList(userId: String) {
        batchListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchListMutLiveData.postValue(
                    ApiResource.success(
                        announcementRepository.getBatchList(
                            userId
                        )
                    )
                )
            } catch (exception: HttpException) {
                batchListMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                batchListMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

    fun sendRequest(request: SendNewMessageRequest) {
        sendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendNewMessageResponce.postValue(
                    ApiResource.success(
                        announcementRepository.sendNewMessageRequest(
                            request
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }

}