package com.kapilguru.student.myClassRoomDetails.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.announcement.newMessage.data.NewMessageResponse
import com.kapilguru.student.myClassRoomDetails.MyClassRoomDetailsRepository
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListRequest
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListResponse
import com.kapilguru.student.myClassRoomDetails.model.*
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyClassDetailsViewModel(private val repository: MyClassRoomDetailsRepository, application: Application) : AndroidViewModel(application) {
    private val TAG = "BatchCompletionReqViewModel"

    var batchApiResponse: MutableLiveData<ApiResource<BatchDetailsResponse>> = MutableLiveData()
    var reviewRequest: MutableLiveData<ReviewRequest> = MutableLiveData(ReviewRequest(0f,"",0,""))
    var reviewResponse: MutableLiveData<ApiResource<ReviewResponse>> = MutableLiveData()
    var batchId: MutableLiveData<String?> = MutableLiveData("")
    val isComplaint: MutableLiveData<Boolean> = MutableLiveData(false)

    var raiseComplaintResponse: MutableLiveData<ApiResource<RaiseComplaintResponse>> = MutableLiveData()
    var refundResponse: MutableLiveData<ApiResource<RefundResponse>> = MutableLiveData()

    var examListApiRes : MutableLiveData<ApiResource<QuestionPaperListResponse>> = MutableLiveData()
    var examListApiReq : MutableLiveData<QuestionPaperListRequest> = MutableLiveData()
    var batchCode : String = ""

    var studyMaterialResponse: MutableLiveData<ApiResource<StudyMaterialResponse>> = MutableLiveData()
    var studyMaterialResponseApi: MutableLiveData<List<StudyMaterialResponseApi>> = MutableLiveData()

    fun getBatchDetails() {
        batchApiResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                batchApiResponse.postValue(ApiResource.success(repository.getBatchDetails(batchId.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                batchApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun raiseComplaint(raiseComplaintRequest: RaiseComplaintRequest){
        raiseComplaintResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                raiseComplaintResponse.postValue(ApiResource.success(repository.raiseComplaint(raiseComplaintRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                raiseComplaintResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            }catch (exception: HttpException) {
                exception.printStackTrace()
                raiseComplaintResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                raiseComplaintResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun updateReview(reviewRequest: ReviewRequest){
        reviewResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reviewResponse.postValue(ApiResource.success(repository.updateReview(reviewRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                reviewResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                reviewResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                reviewResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun refundRequest(refundRequest: RefundRequest){
        refundResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refundResponse.postValue(ApiResource.success(repository.refundRequest(refundRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                refundResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            }catch (exception: HttpException) {
                exception.printStackTrace()
                refundResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                refundResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

    fun getQuestionPaper() {
        examListApiRes.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                examListApiRes.postValue(ApiResource.success(repository.getExamList(examListApiReq.value!!)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            }catch (exception: HttpException) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }catch (exception: IOException) {
                exception.printStackTrace()
                examListApiRes.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }


    fun getStudyMaterial(batchId: String) {
        studyMaterialResponse.postValue(ApiResource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                studyMaterialResponse.postValue(ApiResource.success(repository.getStudyMaterialList(batchId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                studyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                studyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                studyMaterialResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}