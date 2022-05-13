package com.kapilguru.student.referandearn

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityReferAndEarnBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.referandearn.viewModel.ReferAndEarnViewModel
import com.kapilguru.student.referandearn.viewModel.ReferAndEarnViewModelFactory
import java.util.*

class ReferAndEarnActivity : AppCompatActivity() {
    private val TAG = "ReferAndEarnActivity"
    lateinit var viewModel: ReferAndEarnViewModel
    lateinit var binding: ActivityReferAndEarnBinding
    lateinit var dialog: CustomProgressDialog
    var canRefer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        observeViewModelData()
        setClickListeners()
        checkReferEligibility()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, ReferAndEarnViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(ReferAndEarnViewModel::class.java)
        binding.vieModel = viewModel
    }

    private fun setActivityName() {
        binding.actionbar.actvActivityName.text = getString(R.string.refer_and_earn)
    }

    private fun observeViewModelData() {
        observeMyCoursesResponse()
        observeReferResponse()
    }

    private fun observeMyCoursesResponse() {
        viewModel.enrolledCourseResponse.observe(this, Observer { enrolledCourseApiRes ->
            when (enrolledCourseApiRes.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val enrolledCourseList = enrolledCourseApiRes.data?.data
                    if (enrolledCourseList == null || enrolledCourseList.isEmpty()) {
                        informUserToEnrollForCourse()
                        viewModel.canRefer = false
                    } else {
                        viewModel.canRefer = true
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }

        })
    }

    private fun observeReferResponse() {
        viewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    showAppToast(this, "New Referral Sent Successfully")
                    finish()
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

    private fun setClickListeners() {
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnReferNow.setOnClickListener {
            onReferNowClicked()
        }
    }

    fun onReferNowClicked() {
        val mail = viewModel.inviteeEmail.value.toString()
        val mobile = viewModel.inviteeContactNumber.value.toString()
        when {
            binding.referTypeSpinner.selectedItemPosition == 0 -> {
                showAppToast(this, "Please select Referral Type")
            }
            TextUtils.isEmpty(mail) || TextUtils.equals(mail, "null") -> {
                showAppToast(this, "Please enter Email Id")
                return
            }
            viewModel.inviteeEmail.value?.trim()!!.emailValidation() -> {
                showAppToast(this, "Please enter valid Email Id")
                return
            }
            TextUtils.isEmpty(mobile) || TextUtils.equals(mobile, "null") -> {
                showAppToast(this, "Please enter Contact Number")
                return
            }
            viewModel.inviteeContactNumber.value.toString().trim().length < 10 -> {
                showAppToast(this, "Please enter valid Phone Number")
            }
            else -> {
                val pref = StorePreferences(application)
                viewModel.requestApiCall(pref.studentId, binding.referTypeSpinner.selectedItem.toString().lowercase(Locale.getDefault()))
            }
        }
    }

    /*User can refer only when he is enrolled to a course*/
    private fun checkReferEligibility() {
        viewModel.getEnrolledCourses(StorePreferences(this).studentId)
    }

    private fun informUserToEnrollForCourse() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.enroll_and_refer))
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        alertDialogBuilder.show()
    }

    companion object {

        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, ReferAndEarnActivity::class.java)
            activity.startActivity(intent)
        }
    }
}