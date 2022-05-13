package com.kapilguru.student.login

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityMainBinding
import com.kapilguru.student.forgotPassword.ForgotPasswordActivity
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.login.models.LoginResponse
import com.kapilguru.student.login.viewModel.InValidErrors
import com.kapilguru.student.login.viewModel.LoginViewModel
import com.kapilguru.student.login.viewModel.LoginViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.signup.SignUpActivity
import com.kapilguru.student.otpLogin.OTPLoginActivity

class LoginActivity : BaseActivity() {

    lateinit var loginViewModel: LoginViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(LoginViewModel::class.java)
        dataBinding.loginViewModel = loginViewModel
        dataBinding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setMovementMethods()
        setClickListeners()
        observeViewModelData()
    }

    private fun setMovementMethods(){
        dataBinding.tvTermsAndConds.movementMethod = LinkMovementMethod.getInstance()
        dataBinding.tvTermsAndConds.setLinkTextColor(getColor(R.color.blue_2))
    }

    private fun setClickListeners() {
        dataBinding.llSignUp.setOnClickListener {
            launchSignUpActivity()
        }
        dataBinding.buttonOtpLogin.setOnClickListener {
            launchOTPLoginActivity()
        }
    }

    private fun launchSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun launchOTPLoginActivity() {
        startActivity(Intent(this, OTPLoginActivity::class.java))
    }

    private fun observeViewModelData() {
        // API Call for login obserer
        loginViewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dataBinding.customButton.shouldShowLoading(true)
                }

                Status.SUCCESS -> {
                    checkAndStoreTokenToSharedPreferences(it.data)
                    dataBinding.customButton.shouldShowLoading(false)
                }

                Status.ERROR -> {
                    dataBinding.customButton.shouldShowLoading(false)
                    when (it.code) {
                        400 -> {
                            showErrorDialog(resources.getString(R.string.invalid_credentials))
                        }
                        422 -> {
                            showErrorDialog(resources.getString(R.string.user_not_registered))
                        }
                        208 -> { //login exceeded comes in a success response. So not needed here.
                            showErrorDialog(resources.getString(R.string.login_exceeded))
                        }
                        NETWORK_CONNECTIVITY_EROR -> {
                            showErrorDialog(getString(R.string.network_connection_error))
                        }
                        else -> {
                            showErrorDialog(getString(R.string.try_again))
                        }
                    }
                }
            }
        })

        loginViewModel.errorOnValidations.observe(this, Observer { it ->
            when (it) {
                InValidErrors.EMAILINCORRECT -> {
                    showErrorDialog(getString(R.string.invalid_email))
                }
                InValidErrors.PASSWORDINCORRECT -> {
                    showErrorDialog(getString(R.string.invalid_password))
                }
            }
        })
    }

    private fun checkAndStoreTokenToSharedPreferences(loginResponse: LoginResponse?) {
        if(loginResponse?.data?.isStudent ==1){
            val pref = StorePreferences(this)
            loginResponse?.let {
                pref.trainerToken = it.data?.token.toString()
                pref.studentId = it.data?.userId!!
                pref.isProfileUpdated = it.data.isProfileUpdated
                pref.contactNumber = it.data.contactNumber
                pref.isBankUpdated = it.data.isBankUpdated
                pref.isImageUpdated = it.data.isImageUpdated
                pref.userName = it.data.username
                pref.userCode = it.data.userCode
                pref.email = it.data.email
            }
            navigateToHomeActivity()
        }else{
            if(loginResponse?.status ==208){
                showErrorDialog(getString(R.string.login_exceeded))
            }else{
                showErrorDialog("Login Credentials are incorrect")
            }
        }
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { _, _ ->
                    setCancelable(true)
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog?.show()
    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

    fun onForgotPasswordClicked(view: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }
}