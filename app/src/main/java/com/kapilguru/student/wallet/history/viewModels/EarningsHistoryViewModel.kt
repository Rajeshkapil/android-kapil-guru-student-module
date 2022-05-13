package com.kapilguru.student.wallet.history.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.wallet.history.EarningsHistoryRepository
import com.kapilguru.student.wallet.history.model.EarningsHistoryResponseApi
import com.kapilguru.student.wallet.history.model.HistoryPaymentAmountDetailsApi
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class EarningsHistoryViewModel(
    val earningsHistoryRepository: EarningsHistoryRepository,
    appliaction: Application
): AndroidViewModel(appliaction) {

    var resultInfo: MutableLiveData<ApiResource<EarningsHistoryResponseApi>> = MutableLiveData()
    var paymentInfo: MutableLiveData<ApiResource<HistoryPaymentAmountDetailsApi>> = MutableLiveData()
    var earningsRefund: ArrayList<EarningsRefundList> = arrayListOf()
    var earningsReferral: ArrayList<EarningsReferralList> = arrayListOf()
//    var webinarAmountDetails: ArrayList<WebinarAmountResponse> = arrayListOf()

    fun fetchResponse(trainerId: String) {
        resultInfo.value = ApiResource.loading(data= null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resultInfo.postValue(
                    ApiResource.success(
                        earningsHistoryRepository.getEarningsHistory(trainerId)
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                resultInfo.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code)
                )
            } catch (exception: HttpException) {
                resultInfo.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }catch (exception: IOException) {
                resultInfo.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                )
            }
        }
    }


    fun fetchHistoryDetailsAmount(trainerId: String, selectedId: String) {
        paymentInfo.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                paymentInfo.postValue(
                    ApiResource.success(
                        earningsHistoryRepository.getHistoryDetailAmount(trainerId, selectedId)
                    )
                )
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                paymentInfo.postValue(ApiResource.error(data = null, exception.message,exception.code))
            }catch (exception: HttpException) {
                paymentInfo.postValue(ApiResource.error(data = null, exception.message()))
            } catch (exception: IOException) {
                paymentInfo.postValue(ApiResource.error(data = null,exception.message?: "Error Occurred!"))
            }
        }
    }

}