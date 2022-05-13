package com.kapilguru.student.otpLogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityOtploginBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.otpLogin.viewModel.OTPLoginViewModel
import com.kapilguru.student.otpLogin.viewModel.OTPLoginViewModelFactory

class OTPLoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtploginBinding
    lateinit var viewModel: OTPLoginViewModel
    private val TAG = "OTPLoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otplogin)
        viewModel = ViewModelProvider(this, OTPLoginViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(OTPLoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setRequestOTPFragment()
    }

    private fun setRequestOTPFragment() {
        setFragment(RequestOTPFragment())
    }

    fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(R.id.frame_layout_otp_login, fragment).commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_layout_otp_login, fragment).commit()
    }
}