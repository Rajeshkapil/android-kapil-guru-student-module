package com.kapilguru.student.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.PARAM_PROFILE
import com.kapilguru.student.R
import com.kapilguru.student.databinding.DialogOtpBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.networkConnectionErrorDialog
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.profile.data.CheckOTPRequest
import com.kapilguru.student.ui.profile.data.GetOTPRequest
import com.kapilguru.student.ui.profile.data.ProfileData
import com.kapilguru.student.ui.profile.viewModel.ProfileOptionsViewModel

class OTPDialogFragment : DialogFragment() {
    lateinit var profileOptionViewModel : ProfileOptionsViewModel
    lateinit var binding : DialogOtpBinding
    var contactDetails : String?=null
    companion object {
        val TAG = "OTPDialog"
        fun newInstance(profileData: ProfileData): OTPDialogFragment {
            return OTPDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_PROFILE, profileData)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        StorePreferences(requireContext()).contactNumber = "8125344436"
        profileOptionViewModel = ViewModelProvider(requireParentFragment()).get(ProfileOptionsViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_otp,container,false)
        dialog?.setCanceledOnTouchOutside(false);
        setContactDetails()
        binding.tvOtpDesc.text =  getString(R.string.otp_desc)
        observeViewModelData()
        clickListiner()
        return binding.root
    }

    private fun setContactDetails() {
        val profile: ProfileData? = getIntentProfileData()
        profile?.let { profileData ->
            contactDetails = if (profileData.countryCode == "91") {
                profileData.contactNumber
            } else {
                profileData.email_id
            }
        }
    }

    private fun getIntentProfileData(): ProfileData? {
        val profile: ProfileData? = arguments?.getParcelable(PARAM_PROFILE)
        return profile
    }

    fun clickListiner() {
        binding.btnRequestOtp.setOnClickListener{
            getOTPRequest()
            binding.tvOtpDesc.text = getString(R.string.otp_desc)+" "+contactDetails
            binding.otpLayout.visibility = View.VISIBLE
            binding.requestLayout.visibility = View.GONE
        }

        binding.iconClose.setOnClickListener { dialog!!.cancel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatcher()
    }

    private fun observeViewModelData(){
        profileOptionViewModel.getOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    showToast(it.message.toString())
                }

                Status.ERROR -> {
//                    Log.d(TAG,"observeViewModelData getOTPResponce error")
                    showToast("Failed in getting OTP request")
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })
    }

    private fun getOTPRequest(){
        val profile: ProfileData? = getIntentProfileData()
        var getOTPRequest: GetOTPRequest
        profile?.let {profileData ->
            getOTPRequest = if(profileData.countryCode == "91") {
                GetOTPRequest(contactNumber = contactDetails)
            } else {
                GetOTPRequest(userId = StorePreferences(requireContext()).studentId.toString(), emailId = profileData.email_id)
            }
            profileOptionViewModel.getOTP(getOTPRequest)
        }

    }

    private fun enableOTPEditText(){
        binding.etOtp.setFocusable(true)
    }

    private fun addTextWatcher() {
        binding.etOtp.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(otp: Editable?) {
                if(otp?.length ==6){
                    validateOtp(otp)
                }
            }

        })
    }

    private fun validateOtp(otp: Editable) {
        var checkOTPRequest: CheckOTPRequest
        val profile: ProfileData? = getIntentProfileData()
        profile?.let { profileData ->
            checkOTPRequest = if (profileData.countryCode == "91") {
                val contacNo = StorePreferences(requireContext()).contactNumber
                CheckOTPRequest(contactNumber = contacNo, otp = otp.toString())
            } else {
                CheckOTPRequest(emailId = profileData.email_id, otp = otp.toString())
            }
            profileOptionViewModel.checkOTP(checkOTPRequest)
        }
    }

    private fun showToast(text : String){

    }
}