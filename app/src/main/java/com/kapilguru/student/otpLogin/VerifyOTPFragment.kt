package com.kapilguru.student.otpLogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentVerifyOTPBinding
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.login.models.LoginResponseData
import com.kapilguru.student.network.Status
import com.kapilguru.student.otpLogin.viewModel.OTPLoginViewModel
import com.kapilguru.student.preferences.StorePreferences

class VerifyOTPFragment : Fragment() {
    private val TAG = "VerifyOTPFragment"
    lateinit var binding: FragmentVerifyOTPBinding
    val viewModel: OTPLoginViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVerifyOTPBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(requireContext())
        observeViewModelData()
//        setDoAfterTextChangeListener()
        setClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
    }

    private fun observeViewModelData() {
        observeResendOTPResponce()
        observeOtpLoginResponse()
    }

    private fun observeResendOTPResponce() {
        viewModel.otpLoginValidateResponse.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    response.data?.let { otpLoginValidRes ->
                        val result = otpLoginValidRes.otpLoginValidateResData?.get(0)?.result
                        if (result == 1) {
                            showAppToast(requireContext(), getString(R.string.otp_resent))
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeOtpLoginResponse() {
        viewModel.otpLoginResponse.observe(viewLifecycleOwner, Observer { otpLoginResponse ->
            when (otpLoginResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    otpLoginResponse.data?.let { loginResponse ->
                        loginResponse.data?.let { loginResData ->
                            storeTokenToSharedPreferences(loginResData)
                            progressDialog.dismissLoadingDialog()
                        }
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun storeTokenToSharedPreferences(loginResponse: LoginResponseData) {
        val pref = StorePreferences(requireContext())
        pref.trainerToken = loginResponse.token
        pref.studentId = loginResponse.userId
        pref.isProfileUpdated = loginResponse.isProfileUpdated
        pref.contactNumber = loginResponse.contactNumber
        pref.isBankUpdated = loginResponse.isBankUpdated
        pref.userName = loginResponse.username
        navigateToHomeActivity()
    }

    private fun navigateToHomeActivity() {
        activity?.startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finishAffinity()
    }

    private fun setDoAfterTextChangeListener() {
        binding.tietOtp.doAfterTextChanged { otpOrNull ->
            otpOrNull?.let { otp ->
                shouldEnableProceedAndVerify(otp.length == 6)
            }
        }
    }

    private fun shouldEnableProceedAndVerify(shouldEnable: Boolean) {
        binding.btnVerifyAndProceed.isEnabled = shouldEnable
    }

    private fun setText() {
        viewModel.otpLoginValidateRequest.value?.emailOrPhone?.let {emailOrPhone->
            if(emailOrPhone.isValidMobileNo()){
                val mobileNoToShow = getLastDigitsMobileNo(4,emailOrPhone)
                binding.otpSent.text = String.format(getString(R.string.otp_sent_successfully), mobileNoToShow)
            }else{
                val mailIdToShow = getMailWithFirstAndLastCharacters(3,4,emailOrPhone)
                binding.otpSent.text = String.format(getString(R.string.otp_sent_successfully), mailIdToShow)
            }
        }
    }

    private fun setClickListeners() {
        binding.root.setOnClickListener {
            //do nothing consume click
        }
        binding.btnVerifyAndProceed.setOnClickListener {
            onVerifyAndProceedClicked()
        }
        binding.actvResendOtp.setOnClickListener {
            viewModel.sendOTPLoginValidateReq()
        }
        binding.llLogin.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun onVerifyAndProceedClicked(){
        val otp = binding.tietOtp.text
        if(otp.length == 6){
            viewModel.sendOTPLogin()
        }else{
            showAppToast(requireContext(),"Please enter six digit OTP")
        }
    }
}