package com.kapilguru.student.forgotPassword.viewModel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kapilguru.student.forgotPassword.model.*
import com.kapilguru.student.forgotPassword.ForgotPasswordRepository
import com.kapilguru.student.isValidMobileNo
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.CheckOTPResponce
import com.kapilguru.student.ui.profile.data.GetOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPResponce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ForgotPasswordViewModel(private val forgotPasswordRepository: ForgotPasswordRepository) : ViewModel() {
    val TAG = "ForgotPasswordViewModel"
    val contactNo : MutableLiveData<String> = MutableLiveData()
    val otp : MutableLiveData<String> = MutableLiveData()
    val password : MutableLiveData<String> = MutableLiveData()
    val confirmPassword : MutableLiveData<String> = MutableLiveData()
    val errorDescription : MutableLiveData<String> = MutableLiveData()
    val validateMobileResMutLiveData : MutableLiveData<ApiResource<ValidateMobileResponse>> = MutableLiveData()
    val getOTPResponseMutLiveData : MutableLiveData<ApiResource<GetOTPResponce>> = MutableLiveData()
    val checkOTPResponseMutLiveData : MutableLiveData<ApiResource<CheckOTPResponce>> = MutableLiveData()
    val changePasswordResMutLiveData : MutableLiveData<ApiResource<ChangePasswordResponse>> = MutableLiveData()

    fun validateMobile(){
        if(isMobileValid()){
            validateMobileResMutLiveData.value = ApiResource.loading(null)
            viewModelScope.launch(Dispatchers.IO){
                var validateMobileReq = ValidateMobileRequest(contactNo.value!!)
                try{
                    validateMobileResMutLiveData.postValue(ApiResource.success(forgotPasswordRepository.validateMobile(validateMobileReq))
                    )
                }catch (exception : RetrofitNetwork.NetworkConnectionError){
                    validateMobileResMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code=exception.code))
                }catch (exception : HttpException){
                    validateMobileResMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }catch (exception : IOException){
                    validateMobileResMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }
            }
        }
    }

    fun getOTP(){
        getOTPResponseMutLiveData.value = ApiResource.loading(null)
            viewModelScope.launch(Dispatchers.IO) {
                try{
                    val otpRequest = GetOTPRequest(contactNo.value!!)
                    getOTPResponseMutLiveData.postValue(ApiResource.success(forgotPasswordRepository.getOTP(otpRequest)))
                }catch (exception : RetrofitNetwork.NetworkConnectionError){
                    getOTPResponseMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!",code = exception.code))
                }catch (exception : HttpException){
                    getOTPResponseMutLiveData.postValue(
                    ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }catch (exception : IOException){
                    getOTPResponseMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }
            }
    }

    fun isMobileValid() : Boolean{
        if(TextUtils.isEmpty(contactNo.value)){
            errorDescription.postValue("Please enter mobile number")
            return false
        }
        if(!contactNo.value!!.isValidMobileNo()){
            errorDescription.postValue("Please enter valid mobile number")
            return false
        }
        return true
    }

    fun checkOtp(){
        if(isOTPvalid()){
            checkOTPResponseMutLiveData.value = ApiResource.loading(null)
            val checkOTPRequest = CheckOTPRequest(contactNo.value!!,otp.value!!)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    checkOTPResponseMutLiveData.postValue(ApiResource.success(forgotPasswordRepository.checkOTP(checkOTPRequest)))
                } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                    checkOTPResponseMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!", code = exception.code)
                    )
                } catch (exception: HttpException) {
                    checkOTPResponseMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                    )
                } catch (exception: IOException) {
                    checkOTPResponseMutLiveData.postValue(
                        ApiResource.error(data = null, message = exception.message ?: "Error Occurred!")
                    )
                }
            }
        }
    }

    private fun isOTPvalid() : Boolean {
        if(TextUtils.isEmpty(otp.value)){
            errorDescription.postValue("Please enter OTP")
            return false
        }
        if(otp.value?.length == 6){
            return true
        }else{
            errorDescription.postValue("Please enter OTP")
            return false
        }
    }

    fun changePassword(){
       if(isPasswordValid()){
           changePasswordResMutLiveData.value = ApiResource.loading(null)
           val changePasswordReq = ChangePasswordRequest(contactNo.value!!,password.value!!)
           viewModelScope.launch(Dispatchers.IO) {
               try{
                   changePasswordResMutLiveData.postValue(ApiResource.success(forgotPasswordRepository.changePassword(changePasswordReq)))
               }catch (exception : HttpException){
                   changePasswordResMutLiveData.postValue(
                       ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
               }catch (exception : IOException){
                   changePasswordResMutLiveData.postValue(
                       ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
               }
           }
       }
    }

    private fun isPasswordValid() : Boolean{
       if(TextUtils.isEmpty(password.value)) {
           errorDescription.postValue("Please enter Password")
           return false
       }
        if(TextUtils.isEmpty(confirmPassword.value)){
           errorDescription.postValue("Please enter confirm password")
            return false
        }

        if(password.value?.length!! <6){
            errorDescription.postValue("Password should be of minimum 6 characters")
            return false
        }

        if(!TextUtils.equals(password.value,confirmPassword.value)){
            errorDescription.postValue("Password mismatch!! \nPassword and Confirm password must be same")
            return false
        }
        return true
    }
}