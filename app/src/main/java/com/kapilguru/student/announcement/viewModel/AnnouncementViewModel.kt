package com.kapilguru.student.announcement.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.AnnouncementRepository
import com.kapilguru.student.announcement.inbox.data.InboxResponse
import com.kapilguru.student.announcement.inbox.data.LastMessageRequest
import com.kapilguru.student.announcement.newMessage.data.*
import com.kapilguru.student.announcement.sentItems.data.SentItemsResponse
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AnnouncementViewModel(private val announcementRepository: AnnouncementRepository, var contxt: Application) : AndroidViewModel(contxt) {
    private val TAG = "SentItemsViewModel"
    var sentItemsResponse: MutableLiveData<ApiResource<SentItemsResponse>> = MutableLiveData()
    var batchListMutLiveData: MutableLiveData<ApiResource<NewMessageResponse>> = MutableLiveData()
    var adminListMutLiveData: MutableLiveData<ApiResource<AdminMessageResponse>> = MutableLiveData()
    var sendNewMessageResponce: MutableLiveData<ApiResource<SendNewMessageResponce>> = MutableLiveData()
    var subjectMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var messageMutLiveData: MutableLiveData<String> = MutableLiveData("")
    var selectedBatchReceiverId: MutableLiveData<String> = MutableLiveData("")
    var selectedAdminUserId: MutableLiveData<String> = MutableLiveData("")
    var isAdminChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    var resultDat: MutableLiveData<ApiResource<InboxResponse>> = MutableLiveData()
    var commonResponse: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()

    // set initial count as 0
    var inboxItemCount: MutableLiveData<String> = MutableLiveData("0")

    fun getInboxResponce(userId: String) {
        resultDat.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultDat.postValue(ApiResource.success(announcementRepository.getInBoxResponse(userId)))
            } catch (exception: HttpException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                resultDat.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun getBatchList(userId: String) {
        batchListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchListMutLiveData.postValue(ApiResource.success(announcementRepository.getBatchList(userId)))
            } catch (exception: HttpException) {
                batchListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                batchListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getAdminList() {
        adminListMutLiveData.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                adminListMutLiveData.postValue(ApiResource.success(announcementRepository.getAdminList()))
            } catch (exception: HttpException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                adminListMutLiveData.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendBatchRequest(request: SendNewMessageRequest) {
        sendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendNewMessageResponce.postValue(ApiResource.success(announcementRepository.sendNewMessageRequest(request)))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun sendAdminRequest(request: SendAdminMessageRequest) {
        sendNewMessageResponce.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendNewMessageResponce.postValue(
                    ApiResource.success(
                        announcementRepository.sendAdminMessageRequest(request)
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            }catch (exception: HttpException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                exception.printStackTrace()
                sendNewMessageResponce.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun getSentItemsResponse(userId: String) {
        sentItemsResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sentItemsResponse.postValue(
                    ApiResource.success(
                        announcementRepository.getSentItemsResponse(
                            userId
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                sentItemsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun updateLastMessageId(messageId: Int) {
        commonResponse.value = ApiResource.loading(null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                commonResponse.postValue(
                    ApiResource.success(
                        announcementRepository.updateLastMessageId(
                            StorePreferences(contxt).studentId.toString(), LastMessageRequest().apply { lastAnnouncementId = messageId }
                        )
                    )
                )
            } catch (exception: HttpException) {
                exception.printStackTrace()
                commonResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}