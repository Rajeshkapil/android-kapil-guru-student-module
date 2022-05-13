package com.kapilguru.student.announcement.sentItems.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.AnnouncementRepository
import com.kapilguru.student.announcement.sentItems.data.SentItemsResponse
import com.kapilguru.student.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SentItemsViewModel(private val announcementRepository: AnnouncementRepository) : ViewModel() {
    private val TAG = "SentItemsViewModel"

    var sentItemsResponse: MutableLiveData<ApiResource<SentItemsResponse>> = MutableLiveData()


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
            } catch (exception: IOException) {
                exception.printStackTrace()
                sentItemsResponse.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}