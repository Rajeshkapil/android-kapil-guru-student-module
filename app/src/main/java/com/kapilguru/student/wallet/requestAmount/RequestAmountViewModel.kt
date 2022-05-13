package com.kapilguru.student.wallet.requestAmount

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.CommonResponse
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.toBase64
import com.kapilguru.student.wallet.EarningsRepository
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import com.kapilguru.student.wallet.model.EarningsSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class RequestAmountViewModel(
    private val earningsRepository: EarningsRepository,
    application: Application
): AndroidViewModel(application) {

    var isReferralAdded: MutableLiveData<Boolean> = MutableLiveData(false)
    var isRefundAdded:  MutableLiveData<Boolean> = MutableLiveData(false)
    var studentId: Int
    var earningsSummary: MutableLiveData<EarningsSummary> = MutableLiveData(EarningsSummary())
    var earningsRefundList: MutableLiveData<ArrayList<EarningsRefundList>> = MutableLiveData()
    var earningsReferralList: MutableLiveData<ArrayList<EarningsReferralList>> = MutableLiveData()
    var requestMoneyCommonResponse: MutableLiveData<CommonResponse> = MutableLiveData()
    var requestMoneyCommonResponseApi: MutableLiveData<ApiResource<CommonResponse>> = MutableLiveData()
    var totalAmount: MutableLiveData<Double> = MutableLiveData(0.0)


    init {
        val pref = StorePreferences(application)
        studentId = pref.studentId

        isReferralAdded.observeForever {
            setTotalAmount()
        }
        isRefundAdded.observeForever {
            setTotalAmount()
        }

    }

    private fun setTotalAmount() {
            totalAmount.value = 0.0
        if(isReferralAdded.value!!) {
            earningsSummary.value?.referralAmountAvailable?.let { it->
                totalAmount.value = totalAmount.value!!+it
            }

        }
        if(isRefundAdded.value!!) {
            earningsSummary.value?.refundAmountAvailable?.let { it->
                totalAmount.value = totalAmount.value!!+it
            }
        }
    }

    var earnings =  liveData(Dispatchers.IO) {
        emit(ApiResource.loading(data = null))
        try {
            emit(ApiResource.success(data = earningsRepository.getEarnings(studentId.toString())))
        } catch (exception: RetrofitNetwork.NetworkConnectionError) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
        } catch (exception: HttpException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        } catch (exception: IOException) {
            emit(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

     fun requestMoneyApiCall(requestMoneyApi: RequestMoneyApi) {
         requestMoneyCommonResponseApi.value = ApiResource.loading(null)
         viewModelScope.launch(Dispatchers.IO) {
             try {
                 requestMoneyCommonResponseApi.value = (ApiResource.success(data = earningsRepository.requestMoney(requestMoneyApi)))
             } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                 requestMoneyCommonResponseApi.value = (ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",exception.code))
             } catch (exception: HttpException) {
                 requestMoneyCommonResponseApi.value = (ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
             } catch (exception: IOException) {
                 requestMoneyCommonResponseApi.value = (ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
             }
         }
     }

    fun constructData() {
        totalAmount.value?.let { info ->
            if (info > 0) {
                val requestMoneyApi = RequestMoneyApi()
                requestMoneyApi.userId = studentId.toString()
                requestMoneyApi.totalAmount = totalAmount.value!!
                requestMoneyApi.deductedAmount = 0.0
                requestMoneyApi.payableAmount = totalAmount.value!!
                requestMoneyApi.amountFrom = constructAmountFrom()
                requestMoneyApi.referralsAmount = earningsSummary.value?.referralAmountAvailable
                requestMoneyApi.referralsAmount = earningsSummary.value?.refundAmountAvailable
                requestMoneyApi.prizesAmount = earningsSummary.value?.prizesAmountAvailable
                requestMoneyApiCall(requestMoneyApi)
            }
        }
    }

    private fun constructAmountFrom(): String {
        val jsonObject = JsonObject()
        val gson = GsonBuilder().create()
        earningsReferralList.value?.let {
            val myCustomArray = gson.toJsonTree(earningsReferralList.value).asJsonArray
            jsonObject.add("referrals", myCustomArray)
        }?: run{
            jsonObject.add("referrals", null)
        }
        earningsRefundList.value?.let {
           val myCustomArray = gson.toJsonTree(earningsRefundList.value).asJsonArray
            jsonObject.add("refunds", myCustomArray)
        }?: kotlin.run {
            jsonObject.add("refunds", null)
        }
        jsonObject.add("prizes", null)
        return jsonObject.toString().toBase64()
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