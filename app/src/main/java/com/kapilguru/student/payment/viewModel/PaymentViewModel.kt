package com.kapilguru.student.payment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.payment.PaymentRepository
import com.kapilguru.student.payment.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PaymentViewModel(private val repository: PaymentRepository, val context: Application) : AndroidViewModel(context) {
    val initiateTransactionResponse : MutableLiveData<ApiResource<InitiateTransactionResponse>> = MutableLiveData()
    val transactionStatusResponse : MutableLiveData<ApiResource<TransactionStatusResponse>> = MutableLiveData()
    lateinit var mInitiateTransactionRequest: InitiateTransactionRequest

    fun callInitiateTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) {
        mInitiateTransactionRequest = initiateTransactionRequest
        initiateTransactionResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                initiateTransactionResponse.postValue(ApiResource.success(data = repository.callInitiationTransactionRequest(initiateTransactionRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                initiateTransactionResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occurred !",exception.code))
            }catch (exception: HttpException) {
                initiateTransactionResponse.postValue(ApiResource.error(data = null,exception.message() ?: "Error Occurred !"))
            } catch (exception: IOException) {
                initiateTransactionResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occurred !"))
            }
        }
    }

    fun callTransactionStatusApi(transactionStatusRequest : TransactionStatusRequest) {
        transactionStatusResponse.postValue(ApiResource.loading(data = null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionStatusResponse.postValue(ApiResource.success(data = repository.callTransactionStatusRequest(transactionStatusRequest)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                exception.printStackTrace()
                transactionStatusResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occured !",exception.code))
            } catch (exception: HttpException) {
                exception.printStackTrace()
                transactionStatusResponse.postValue(ApiResource.error(data = null,exception.message() ?: "Error Occured !"))
            } catch (exception: IOException) {
                exception.printStackTrace()
                transactionStatusResponse.postValue(ApiResource.error(data = null,exception.message ?: "Error Occured !"))
            }
        }
    }
}