package com.kapilguru.student.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.wallet.model.EarningsInfo
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import com.kapilguru.student.wallet.model.EarningsSummary

import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class EarningsViewModel(private val earningsRepository: EarningsRepository,
              application: Application): AndroidViewModel(application) {

    var studentId: Int
    var earningsInfo: MutableLiveData<EarningsInfo> = MutableLiveData()
    var earningsSummary: MutableLiveData<EarningsSummary> = MutableLiveData()
    var earningsRefundList: MutableLiveData<ArrayList<EarningsRefundList>> = MutableLiveData()
    var earningsReferralList: MutableLiveData<ArrayList<EarningsReferralList>> = MutableLiveData()

    init {
        val pref = StorePreferences(application)
        studentId = pref.studentId
    }

    var earnings =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = earningsRepository.getEarnings(studentId.toString())))
        } catch (exception: RetrofitNetwork.NetworkConnectionError) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
        }catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


   /* earningsApiResponse.value = ApiResource.loading(data = null)
    viewModelScope.launch {
        try {
            earningsApiResponse.postValue(
                ApiResource.success(
                    earningsRepository.getearningList(trainerId.toString())
                )
            )
        } catch (e: HttpException) {
            earningsApiResponse.postValue(
                ApiResource.error(
                    data = null,
                    message = e.code().toString() ?: "Error Occurred!"
                )
            )
        }
    }    */
}