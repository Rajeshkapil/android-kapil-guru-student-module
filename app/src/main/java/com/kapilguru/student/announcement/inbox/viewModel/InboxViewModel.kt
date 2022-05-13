package com.kapilguru.student.announcement.inbox.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.AnnouncementRepository
import com.kapilguru.student.announcement.inbox.data.InboxResponse
import com.kapilguru.student.network.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InboxViewModel(private val announcementRepository: AnnouncementRepository) : ViewModel() {
    private val TAG = "InboxViewModel"

    var resultDat: MutableLiveData<ApiResource<InboxResponse>> = MutableLiveData()

    // set initial count as 0
    var inboxItemCount: MutableLiveData<String> = MutableLiveData("0")

    fun getInBoxResponse(userId: String) {
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
}