package com.kapilguru.student.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityProfileOverViewBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.changePassword.ChangePasswordActivity
import com.kapilguru.student.ui.profile.bank.BankDetailsActivity
import com.kapilguru.student.ui.profile.viewModel.ProfileOptionsFactory
import com.kapilguru.student.ui.profile.viewModel.ProfileOptionsViewModel
import com.kapilguru.student.ui.profileInfo.view.ProfileDetailsActivity

class ProfileOverViewActivity : AppCompatActivity() {
    private val TAG = "ProfileOverViewActivity"
    lateinit var profileOptionsViewModel: ProfileOptionsViewModel
    lateinit var binding: ActivityProfileOverViewBinding
    lateinit var dialog: CustomProgressDialog
    var isOTPDialogLaunchedForProfile = false
    var isOTPDialogLaunchedForBankDetails = false
    var isOTPDialogLaunchedForChangePassword = false
    lateinit var changePasswordDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        observeViewModelData()
        getProfileData()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        setUpdateProfileVisibility()
    }

    private fun initLateInitVariables() {
        profileOptionsViewModel = ViewModelProvider(this, ProfileOptionsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(ProfileOptionsViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_over_view)
        binding.lifecycleOwner = this
        binding.viewModel = profileOptionsViewModel
        dialog = CustomProgressDialog(this)
        createChangePasswordDialog()
    }

    private fun createChangePasswordDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)
            .setMessage(R.string.change_password_message)
            .setPositiveButton(R.string.request_otp) { dialog, which ->
                launchChangePasswordActivity()
                dialog?.dismiss()
            }
            .setNegativeButton(R.string.Cancel) { dialog, which -> dialog?.dismiss() }
        changePasswordDialog = alertDialogBuilder.create()
    }

    private fun launchChangePasswordActivity() {
        startActivity(Intent(this, ChangePasswordActivity::class.java))
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.title_profile)
    }

    fun getProfileData() {
        val pref = StorePreferences(this)
        profileOptionsViewModel.getProfileData(pref.studentId.toString())
    }

    fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnChangePassword.setOnClickListener {
            launchChangePasswordActivity()
        }
        binding.ivEditProfile.setOnClickListener {
            launchProfileInfoActivity()
        }
        binding.ivBankDetails.setOnClickListener {
            launchBankDetailsActivity()
        }
    }

    private fun setUpdateProfileVisibility() {
        if (StorePreferences(this).isProfileUpdated != 1) {
            showUpdateProfileView()
            binding.actvUpateProfile.visibility = View.VISIBLE
        } else {
            binding.actvUpateProfile.visibility = View.GONE
        }
    }

    private fun showUpdateProfileView() {
        val textView = binding.actvUpateProfile
        textView.isSingleLine = true
        textView.setHorizontallyScrolling(true)
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = -1
        textView.isSelected = true
    }

    fun observeViewModelData() {
        observeProfileData()
        observeCheckOTPResponse()
    }

    private fun observeProfileData() {
        profileOptionsViewModel.profileDataResponse.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    binding.profileData = it?.data?.data?.get(0)
                    it?.data?.data?.let { it ->
                        profileOptionsViewModel.profileData = it[0]
                    }
                    profilePercentage()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observeCheckOTPResponse() {
        profileOptionsViewModel.checkOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status?.equals(200)!!) {
                        hideOTPFragment()
                        when {
                            isOTPDialogLaunchedForProfile -> launchProfileInfoActivity()
                            isOTPDialogLaunchedForBankDetails -> launchBankDetailsActivity()
                            isOTPDialogLaunchedForChangePassword -> launchChangePasswordActivity()
                        }
                    } else {
                        showToast("Enter Correct OTP")
                    }
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when(it.code) {
                       NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun profilePercentage() {
        val pref = StorePreferences(this)
        pref.let {
            var percentage = 0
            if (it.isImageUpdated == 1) percentage += 10
            if (it.isBankUpdated == 1) percentage += 20
            if (it.isProfileUpdated == 1) percentage += 70

            profileOptionsViewModel.profilePercentage.value = percentage
        }
    }

    private fun hideOTPFragment() {
        val fragment: Fragment? = supportFragmentManager.findFragmentByTag(OTPDialogFragment.TAG)
        if (fragment != null) {
            val df: DialogFragment = fragment as DialogFragment
            df.dismiss()
        }
    }

    private fun launchProfileInfoActivity() {
        startActivity(Intent(this, ProfileDetailsActivity::class.java))
    }

    private fun launchBankDetailsActivity() {
        startActivity(Intent(this, BankDetailsActivity::class.java))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object{
        fun launchActivity(activity : Activity){
            val intent = Intent(activity,ProfileOverViewActivity::class.java)
            activity.startActivity(intent)
        }
    }
}