package com.kapilguru.student.forgotPassword

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentGetOtpBinding
import com.kapilguru.student.forgotPassword.viewModel.ForgotPasswordViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.showSingleButtonErrorDialog

class GetOtpFragment : Fragment() {
    val TAG = "GetOtpFragment"

    val viewModel : ForgotPasswordViewModel by activityViewModels()
    lateinit var binding : FragmentGetOtpBinding
    lateinit var dialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        observeViewModelData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_get_otp,container,false)
        binding.viewModel = viewModel
        setClickListerns()
        return binding.root
    }

    private fun observeViewModelData(){
        viewModel.validateMobileResMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING ->{
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS ->{
                    dialog.dismissLoadingDialog()
                    if(it.data?.data?.contact_count == 1){
                        viewModel.getOTP()
                    }else{
                        viewModel.errorDescription.postValue("Mobile number not registered")
                    }
                }

                Status.ERROR ->{
                    dialog.dismissLoadingDialog()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(requireContext(),getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
        viewModel.getOTPResponseMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS ->{
                    dialog.dismissLoadingDialog()
                    launchValidateOTPFragment()
                }

                Status.ERROR ->{
                    dialog.dismissLoadingDialog()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR ->
                            showSingleButtonErrorDialog(requireContext(),getString(R.string.network_connection_error))
                    }
                }
            }
        })
    }

    private fun launchValidateOTPFragment(){
        val fragment = parentFragmentManager.findFragmentByTag(ValidateOtpFragment.FragTAG)
        if(fragment != null){
            return
        }
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit)
            .add(R.id.fl_forgot_password, ValidateOtpFragment.noInstance(), ValidateOtpFragment.FragTAG)
            .addToBackStack(null).commit()
    }

    private fun setClickListerns(){
        binding.getOtp.setOnClickListener {
            viewModel.validateMobile()
        }
        binding.acivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun noInstance() = GetOtpFragment()
    }
}