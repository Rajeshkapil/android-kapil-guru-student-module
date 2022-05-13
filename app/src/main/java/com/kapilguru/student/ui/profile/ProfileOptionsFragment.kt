package com.kapilguru.student.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentProfileOptionsBinding
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.changePassword.ChangePasswordActivity
import com.kapilguru.student.ui.profile.bank.BankDetailsActivity
import com.kapilguru.student.ui.profile.viewModel.ProfileOptionsFactory
import com.kapilguru.student.ui.profile.viewModel.ProfileOptionsViewModel
import com.kapilguru.student.ui.profileInfo.view.ProfileDetailsActivity
import android.app.AlertDialog.Builder as AlertDialogBuilder

class ProfileOptionsFragment : Fragment() {
    val TAG = "ProfileOptionsFragment"
    lateinit var profileOptionsViewModel: ProfileOptionsViewModel
    lateinit var profileOptionsBinding: FragmentProfileOptionsBinding
    lateinit var dialog: CustomProgressDialog
    lateinit var dialogFragmentCustom: DialogFragmentCustom

    //    var isProfileUpdated: Boolean = false
    var isOTPDialogLaunchedForProfile = false
    var isOTPDialogLaunchedForBankDetails = false
    var isOTPDialogLaunchedForChangePassword = false
    lateinit var changePasswordDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileOptionsViewModel = ViewModelProvider(this, ProfileOptionsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(ProfileOptionsViewModel::class.java)
        dialog = CustomProgressDialog(requireContext())
//        dialogFragmentCustom = DialogFragmentCustom()
        createChangePasswordDialog()
        observeViewModelData()
        getProfileData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shouldShowActionBarSearch(true)
//        ((activity) as HomeActivity).fetchLatestNotification()
    }

    private fun shouldShowActionBarSearch(shouldShowActionSearchBar: Boolean){
        ((activity) as HomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }

    private fun createChangePasswordDialog() {
        val alertDialogBuilder = AlertDialogBuilder(requireContext())
        alertDialogBuilder.setCancelable(false)
            .setMessage(R.string.change_password_message)
            .setPositiveButton(R.string.request_otp, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    launchChangePasswordActivity()
                    dialog?.dismiss()
                }
            })
            .setNegativeButton(R.string.Cancel, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
        changePasswordDialog = alertDialogBuilder.create()
    }

    private fun launchChangePasswordActivity() {
        startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
    }

    fun getProfileData() {
        val pref = StorePreferences(requireContext())
        profileOptionsViewModel.getProfileData(pref.studentId.toString())
    }

    fun observeViewModelData() {
        observeProfileData()
        observeCheckOTPResponce()
    }

    private fun observeProfileData() {
        profileOptionsViewModel.profileDataResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
//                    showDialogFragment()
                }
                Status.SUCCESS -> {
//                    dismissDialogFragment()
                    dialog.dismissLoadingDialog()
                    profileOptionsBinding.profileData = it?.data?.data?.get(0)
                    it?.data?.data?.let { it ->
                        profileOptionsViewModel.profileData = it[0]
                    }
                    profilePercentage()
//                    dialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
//                    dismissDialogFragment()
                }
            }

        })
    }

    private fun observeCheckOTPResponce() {
        profileOptionsViewModel.checkOTPResponce.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
//                    showDialogFragment()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
//                    dismissDialogFragment()
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
//                    dismissDialogFragment()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        profileOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_options, container, false)
        profileOptionsBinding.lifecycleOwner = viewLifecycleOwner
        profileOptionsBinding.viewModel = profileOptionsViewModel
        setClickListeners()
        return profileOptionsBinding.root
    }

    override fun onResume() {
        super.onResume()
        setUpdateProfileVisibility()
    }

    private fun setUpdateProfileVisibility() {
        if (StorePreferences(requireContext()).isProfileUpdated != 1) {
            showUpdateProfileView()
            profileOptionsBinding.actvUpateProfile.visibility = View.VISIBLE
        } else {
            profileOptionsBinding.actvUpateProfile.visibility = View.GONE
        }
    }

    private fun showUpdateProfileView() {
        val textView = profileOptionsBinding.actvUpateProfile
        textView.isSingleLine = true
        textView.setHorizontallyScrolling(true)
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = -1
        textView.isSelected = true
    }

    fun setClickListeners() {
        profileOptionsBinding.btnChangePassword.setOnClickListener {
            launchChangePasswordActivity()
//            setOTPDialogLaunchedFor(false, false, true)
//            showOTPFragment()
        }
        profileOptionsBinding.ivEditProfile.setOnClickListener {
            launchProfileInfoActivity()
//            setOTPDialogLaunchedFor(true, false, false)
//            showOTPFragment()
        }
        profileOptionsBinding.ivBankDetails.setOnClickListener {
            launchBankDetailsActivity()
//            setOTPDialogLaunchedFor(false, true, false)
//            showOTPFragment()
        }
    }

    private fun setOTPDialogLaunchedFor(isForProfile: Boolean, isForBank: Boolean, isForChangePassword: Boolean) {
        isOTPDialogLaunchedForProfile = isForProfile
        isOTPDialogLaunchedForBankDetails = isForBank
        isOTPDialogLaunchedForChangePassword = isForChangePassword
    }

    private fun showOTPFragment() {
        OTPDialogFragment.newInstance(profileData = profileOptionsViewModel.profileData).show(childFragmentManager, OTPDialogFragment.TAG)
    }

    private fun hideOTPFragment() {
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(OTPDialogFragment.TAG)
        if (fragment != null) {
            val df: DialogFragment = fragment as DialogFragment
            df.dismiss()
        }
    }

    private fun launchProfileInfoActivity() {
        startActivity(Intent(requireContext(), ProfileDetailsActivity::class.java))
    }

    private fun launchBankDetailsActivity() {
        startActivity(Intent(requireContext(), BankDetailsActivity::class.java))
    }

    private fun showChangePasswordDialog() {
        changePasswordDialog.let { dialog ->
            dialog.show()
        }
    }

    private fun profilePercentage() {
        val pref = context?.let { StorePreferences(it) }
        pref?.let {
            var percentage = 0
            if (it.isImageUpdated == 1) percentage += 10
            if (it.isBankUpdated == 1) percentage += 20
            if (it.isProfileUpdated == 1) percentage += 70

            profileOptionsViewModel.profilePercentage.value = percentage
        }
    }

    fun showDialogFragment(){
        activity?.let { it->
            val abc: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE)
            abc?.let {

            }?: kotlin.run {
                val fm: FragmentManager = it.supportFragmentManager
//                if (activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE) == null)
                dialogFragmentCustom.show(fm, DIALOG_FRAGMENT_TAG_PROFILE)
            }
        }
    }

    fun dismissDialogFragment() {
        val fm: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG_PROFILE)
        fm?.let {it->
            val abc = it as DialogFragmentCustom
            abc.dismiss()
//            Log.d(TAG, "dismissDialogFragment: DISSMISS")
        }?:run{
//            Log.d(TAG, "dismissDialogFragment: whattt")
            activity?.supportFragmentManager?.executePendingTransactions()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        dismissDialogFragment()
    }
}