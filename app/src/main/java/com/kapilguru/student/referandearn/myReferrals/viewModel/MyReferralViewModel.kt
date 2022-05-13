package com.kapilguru.student.referandearn.myReferrals.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.referandearn.myReferrals.MyReferralRepository
import com.kapilguru.student.referandearn.myReferrals.model.MyReferralResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyReferralViewModel(private val repository : MyReferralRepository) : ViewModel() {
    private val TAG = "MyReferralViewModel"
    val myReferralsApiResponse : MutableLiveData<ApiResource<MyReferralResponse>> = MutableLiveData()

    fun getMyReferrals(trainerId : String){
        myReferralsApiResponse.value = ApiResource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                myReferralsApiResponse.postValue(ApiResource.success(data = repository.getMyReferrals(trainerId)))
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                myReferralsApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
            }catch (exception: HttpException) {
                myReferralsApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
                myReferralsApiResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
}