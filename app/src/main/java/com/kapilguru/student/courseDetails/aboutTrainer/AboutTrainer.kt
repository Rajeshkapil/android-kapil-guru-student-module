package com.kapilguru.student.courseDetails.aboutTrainer

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.Companion.loadGlideImage
import com.kapilguru.student.courseDetails.model.ContactTrainerRequest
import com.kapilguru.student.courseDetails.model.Course
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_about_trainer.*
import kotlinx.android.synthetic.main.custom_key_value_view.view.*


class AboutTrainer : BaseActivity() {

    var contactNumber:String?=null
    var  courseInfo: Course? =null
    lateinit var viewModel:AboutTrainerViewModel
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_trainer)
        viewModel = ViewModelProvider(this, AboutTrainerViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AboutTrainerViewModel::class.java)
        dialog = CustomProgressDialog(this)
        setActionbarBackListener(this, custom_action_bar, getString(R.string.about_trainer_title))
        setTrainerData()
        clickListeners()
        observeData()
    }

    private fun observeData() {
        viewModel.contactTrainerResponseAPi.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
//                    contactTrainerDialog()
                    askForphonePermission()
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

    private fun contactTrainerDialog() {
        val fm: FragmentManager = supportFragmentManager
        val contactTrainerDialogFragment = ContactTrainerDialogFragment()
        val bundle = Bundle()
        bundle.putString(PARAM_CONTACT_TRAINER_NUMBER, courseInfo?.contact)
        bundle.putString(PARAM_CONTACT_TRAINER_NAME, courseInfo?.trainerName)
        contactTrainerDialogFragment.arguments = bundle
        contactTrainerDialogFragment.show(fm, "contact_trainer_dialog")
    }

    private fun clickListeners() {
        val pref = StorePreferences(this)
        contactTrainer.setOnClickListener {
            viewModel.contactTrainer(
                ContactTrainerRequest().apply {
                    this.contactNumber = pref.contactNumber
                    this.countryCode = 91
                    this.courseId = courseInfo?.id!!
                    this.emailId = pref.email
                    this.fullName = pref.userName
                    this.studentId = pref.studentId
                    this.trainerId = courseInfo?.trainerId!!
                }
            )
        }
    }

    private fun setTrainerData() {
        courseInfo= intent.getParcelableExtra("PARAM_ABOUT_TRAINER")
        courseInfo?.let { it->
            viewBestTrainer.visibility = if (it.courseBadgeId == null)  View.GONE else  View.VISIBLE
            trainerImage.loadGlideImage(it.courseImage)
            tainerValue.text = it.trainerName
            experienceValue.text_key.text = getString(R.string.experience)
            experienceValue.text_value.text = it.trainersYearOfExp.toString().plus(" Years")
            trainerStudents.text_key.text = resources.getText(R.string.trained_students_no_br)
            trainerStudents.text_value.text = it.totalNoOfStudentsTrained.toString()
            about_trainer_value.text = Html.fromHtml(it.aboutTrainer.toString().fromBase64())
            contactNumber = it.contact.toString()
        }
    }

    private fun askForphonePermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.CALL_PHONE).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                p0?.let { multiplePermissionsReport ->
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        contactTrainerDialog()
                    } else {
                        showAppToast(this@AboutTrainer,"Requires Camera Permission")
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                showAppToast(this@AboutTrainer,"Please grant phone call Permission")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }).check()
    }
}