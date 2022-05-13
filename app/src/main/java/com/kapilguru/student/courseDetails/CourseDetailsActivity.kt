package com.kapilguru.student.courseDetails

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.courseDetails.aboutTrainer.AboutTrainer
import com.kapilguru.student.courseDetails.batchList.BatchList
import com.kapilguru.student.courseDetails.courseSyllabus.CourseSyllabusActivity
import com.kapilguru.student.courseDetails.model.BatchesItem
import com.kapilguru.student.courseDetails.model.ContactTrainerRequest
import com.kapilguru.student.courseDetails.model.Course
import com.kapilguru.student.courseDetails.model.EnrolledCourseResponse
import com.kapilguru.student.courseDetails.review.model.StudentReviewedData
import com.kapilguru.student.courseDetails.review.ui.review.ReviewActivity
import com.kapilguru.student.customUI.CommonDialogFragment
import com.kapilguru.student.databinding.ActivityCourseDetailsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_about_trainer.*
import kotlinx.android.synthetic.main.custom_key_value_view.view.*


class CourseDetailsActivity : BaseActivity() {

    lateinit var viewModel: CourseDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var dataBinding: ActivityCourseDetailsBinding
    private lateinit var requestBatchDialog: CommonDialogFragment
    var courseId: String? = ""
    var userId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_course_details)
        viewModel = ViewModelProvider(this, CourseDetailsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(CourseDetailsViewModel::class.java)
        dataBinding.model = viewModel
        viewModel.courseId.value = intent.getStringExtra(PARAM_COURSE_ID)

        customActionBarSetUp()
        dataBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        clickListeners()
        viewModelObservers()
        setRating()
    }

    private fun clickListeners() {
        dataBinding.upcomingBatches.setOnClickListener {
            navigateToBatches()
        }

        dataBinding.courseSyllabus.setOnClickListener {
            startActivity(Intent(this, CourseSyllabusActivity::class.java).apply {
                putExtra(PARAM_COURSE_SYLLABUS, viewModel.course.value)
            })
        }


        dataBinding.courseDescription.setOnClickListener {
            startActivity(Intent(this, CourseDescriptionActivity::class.java).apply {
                putExtra(PARAM_COURSE_DESCRIPTION, viewModel.course.value)
            })
        }

        dataBinding.trainingFeatures.setOnClickListener {
            startActivity(Intent(this, TrainingFeatures::class.java))
        }

        dataBinding.aboutTrainer.setOnClickListener {
            naviagteToAboutTrainer()
        }

        dataBinding.tainerValue.setOnClickListener {
            naviagteToAboutTrainer()
        }

        dataBinding.review.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java).apply {
                putExtra("PARAM_COURSE_ID", courseId)
            })
        }

        dataBinding.btnEnrollOrAddBatch.setOnClickListener {
            navigateToBatches()
        }

        dataBinding.certificate.setOnClickListener {
            startActivity(Intent(this, CertificateActivity::class.java))
        }

        dataBinding.btnContactTrainer.setOnClickListener {
//            onContactTrainerClick()
            getPermissionFromUser()
        }
    }


    private fun onContactTrainerClick() {
        val pref = StorePreferences(this)
        viewModel.contactTrainer(
            ContactTrainerRequest().apply {
                this.contactNumber = pref.contactNumber
                this.countryCode = 91
                this.courseId = viewModel.course.value?.id!!
                this.emailId = pref.email
                this.fullName = pref.userName
                this.studentId = pref.studentId
                this.trainerId = viewModel.course.value?.trainerId!!
            }
        )
    }

    private fun getPermissionFromUser() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Would you like to share your contact Details with Trainer")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                onContactTrainerClick()
            })
            .setNegativeButton("No",DialogInterface.OnClickListener { dialog, id ->

            })
        val alert: android.app.AlertDialog? = builder.create()
        alert?.show()
    }


    private fun askForPhonePermission() {
        Dexter.withContext(this@CourseDetailsActivity)
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    contactTrainerDialog()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showAppToast(this@CourseDetailsActivity.baseContext, "Please,Grant Phone Permission")
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    showAppToast(this@CourseDetailsActivity.baseContext, "Please, Grant Phone Permission")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }) .onSameThread()
            .withErrorListener { p0 ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }.check()
    }

    private fun contactTrainerDialog() {
        val fm: FragmentManager = supportFragmentManager
        val contactTrainerDialogFragment = ContactTrainerDialogFragment()
        val bundle = Bundle()
        bundle.putString(PARAM_CONTACT_TRAINER_NUMBER, viewModel.course.value?.contact)
        bundle.putString(PARAM_CONTACT_TRAINER_NAME, viewModel.course.value?.trainerName)
        contactTrainerDialogFragment.arguments = bundle
        contactTrainerDialogFragment.show(fm, "contact_trainer_dialog")
    }

    private fun naviagteToAboutTrainer() {
        startActivity(Intent(this, AboutTrainer::class.java).apply {
            putExtra("PARAM_ABOUT_TRAINER", viewModel.course.value)
        })
    }

    private fun navigateToBatches() {
        startActivity(Intent(this, BatchList::class.java).apply {
            putParcelableArrayListExtra(PARAM_BATCHES_LIST, viewModel.batchesList)
            putExtra(PARAM_COURSE_ID, viewModel.courseId.value)
            putExtra(PARAM_IS_ENROLLED, viewModel.isCourseEnrolled.value)
        })
    }

    private fun viewModelObservers() {
        fetchCourseDetails()
        viewModel.resultDat.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    setCourseInfo(it.data?.allData?.course)
                    setBatchsInfo(it.data?.allData?.batches)
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
//                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
//                        showErrorDialog(getString(R.string.try_again))
                    }

                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })


        viewModel.enrolledCourseResponse.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    val enrolledCourse: EnrolledCourseResponse? = it.data
                    enrolledCourse?.let { response ->
                        viewModel.alreadyPurchasedData = response.data?.filter { it ->
                            it.studentId == userId && it.courseId == courseId?.toInt()
                        }
                        viewModel.alreadyPurchasedData?.let {
                            if (it.isNotEmpty()) {
                                viewModel.isCourseEnrolled.value = true
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    when (it.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            progressDialog.dismissLoadingDialog()
                            showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                        }
                        401 -> {
                            sessionLogoutFragment()
                        }
                        else ->{
                            showSingleButtonErrorDialog(this, getString(R.string.try_again))
                        }
                    }
                }
            }

        })


        viewModel.contactTrainerResponseAPi.observe(this, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
//                    contactTrainerDialog()
                    askForPhonePermission()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }

        })

        viewModel.courseId.value?.let {
            viewModel.getStudentsReviewsList(it)
        }

        viewModel.studentListReviewResponse.observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    val response = it.data?.data
                    response?.let { it ->
                        viewModel.studentListReviewResponseData = it
                        viewModel.calculateRating(it)
                        setNumberOfRatedStudents(it)
                    }

                }
                Status.ERROR -> {

                }
            }
        })

    }

    private fun setNumberOfRatedStudents(it: List<StudentReviewedData>) {
        viewModel.studentRatingCount.observe(this, Observer { count ->
            count?.let {
                if (count == 1) {
                    dataBinding.studentNUmber.text = "${count} Student"
                } else {
                    dataBinding.studentNUmber.text = "${count} Students"
                }
            } ?: run {
                dataBinding.studentNUmber.text = "${count} Student"
            }

        })
    }

    private fun sessionLogoutFragment() {
        val fm: FragmentManager = supportFragmentManager
        val sessionLogOutDialogFragment = SessionLogOutDialogFragment()
        sessionLogOutDialogFragment.show(fm, "session_logout_dialog")
    }

    private fun fetchCourseDetails() {
        courseId = intent.getStringExtra(PARAM_COURSE_ID)
        userId = StorePreferences(this).studentId
        viewModel.getCourseDetails(courseId.toString(), userId.toString())
    }

    private fun setBatchsInfo(batches: List<BatchesItem>?) {
        batches?.let {
            viewModel.batchesList = it as ArrayList<BatchesItem>
            dataBinding.model = viewModel
        }
    }

    private fun setCourseInfo(course: Course?) {
        course?.let {
            dataBinding.language.text_value.text = getSelectedLanguagesString(application, course.language?.fromBase64()!!)
            viewModel.course.value = course
        }
    }

    private fun setRating() {
        viewModel.rating.observe(this, Observer { rating ->
            rating?.let {
                dataBinding.ratingBar.rating = it
            } ?: run {
                dataBinding.ratingBar.rating = 0.0f
            }
        })
    }

    private fun customActionBarSetUp() {
        setActionbarBackListener(this, dataBinding.customActionBar.root, getString(R.string.course_details))
    }

    companion object {
        fun reLaunchActivityToCheckRegisterStatus(context: Context) {
            val intent = Intent(context, CourseDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fetchCourseDetails()
    }

}