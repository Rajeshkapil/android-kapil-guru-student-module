package com.kapilguru.student.demoLecture.demoLectureDetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityDemoLectureDetailsBinding
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureDetailsResData
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterStatusRequest
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureRegisterStatusResData
import com.kapilguru.student.demoLecture.demoLectureDetails.viewModel.DemoLectureDetailsViewModel
import com.kapilguru.student.demoLecture.demoLectureDetails.viewModel.DemoLectureDetailsViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.videoCall.VideoCallInterfaceImplementation
import com.kapilguru.student.webinar.model.LanguageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DemoLectureDetailsActivity : AppCompatActivity() {
    private val TAG = "GuestLecDetailsAct"
    lateinit var binding: ActivityDemoLectureDetailsBinding
    lateinit var viewModel: DemoLectureDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var exoPlayerView : PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo_lecture_details)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, DemoLectureDetailsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(DemoLectureDetailsViewModel::class.java)
        val demoLecture = intent.getIntExtra(ACTIVE_DEMO_LECTURE, -1)
        viewModel.demoLectureIntent = demoLecture
        exoPlayerView = binding.exoVideoView
        binding.viewModel = viewModel
        setActivityName()
        observeViewModelData()
        fetchDemoLectureDetails()
        setClickListeners()
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.demo_lecture)
    }

    private fun observeViewModelData() {
        observeDemoLectureRegisterStatus()
        observeDemoLectureDetailsApiRes()
        observeDemoLectureRegisterResponse()
    }

    private fun observeDemoLectureRegisterStatus() {
        viewModel.demoLectureRegisterStatusApiRes.observe(this, Observer { demoLectureRegisterStatusApiRes ->
            when (demoLectureRegisterStatusApiRes.status) {
                Status.LOADING -> {
                    // do nothing
                }
                Status.SUCCESS -> {
                    val demoLectureRegisterStatusResDataList = demoLectureRegisterStatusApiRes.data?.demoLectureRegisterStatusResData
                    onDemoLectureRegisterStatusResponseReceived(demoLectureRegisterStatusResDataList)
                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun onDemoLectureRegisterStatusResponseReceived(demoLectureRegisterStatusResDataList: List<DemoLectureRegisterStatusResData>?) {
        var demoLectureRegisterStatusResData: DemoLectureRegisterStatusResData? = null
        demoLectureRegisterStatusResDataList?.let {
            if (it.isNotEmpty()) {
                demoLectureRegisterStatusResData = it[0]
            }
        }
        demoLectureRegisterStatusResData?.let {
            if (it.status == "Registered") {

                setRegisterVisibility(true)
                setGoLiveVisibility(true)
            } else {
                setRegisterVisibility(false)
                setGoLiveVisibility(false)
            }
        } ?: kotlin.run {
            setRegisterVisibility(false)
            setGoLiveVisibility(false)
        }
    }

    private fun observeDemoLectureDetailsApiRes() {
        viewModel.demoLectureDetailsApiRes.observe(this, Observer { webinarDetailsApiRes ->
            when (webinarDetailsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    webinarDetailsApiRes.data?.let { demolectureDetailsApiRes ->
                        demolectureDetailsApiRes.demoLectureDataList?.let { demoLectureResData ->
                            try {
                                viewModel.demoLectureDetailsApi.value = demoLectureResData[0]
                                (application as MyApplication).getLanguages(object : MyApplication.FetchLanguagesListener {
                                    override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
                                        GlobalScope.launch {
                                            withContext(Dispatchers.Main) {
                                                setLanguagesText(languagesDataList, demoLectureResData[0])
                                            }
                                        }
                                    }
                                })
//                                setGoLiveVisibility()
                                fetchRegisterStatus()
                                setVideo()
                            } catch (exception: IndexOutOfBoundsException) {

                            }
                            progressDialog.dismissLoadingDialog()
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(webinarDetailsApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    private fun observeDemoLectureRegisterResponse() {
        viewModel.demoLectureRegisterApiResponse.observe(this, Observer { demoLectureRegisterApiResponse ->
            when (demoLectureRegisterApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    if (demoLectureRegisterApiResponse.data?.demoLectureRegisterResData?.insertId != null) {
                        showSingleButtonErrorDialog(this,getString(R.string.registered_successfully))
                        setRegisterVisibility(true)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(demoLectureRegisterApiResponse.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun setLanguagesText(languagesList: ArrayList<LanguageData>, demoLectureDetailsResData: DemoLectureDetailsResData) {
        val stringToShow = getSelectedLanguagesStringToShow(languagesList, demoLectureDetailsResData.languages.fromBase64())
        binding.actvLanguagesValue.text = stringToShow
    }

    private fun getSelectedLanguagesStringToShow(languagesList: java.util.ArrayList<LanguageData>, selectedLanguageIds: String): String {
        val selectedLanguageNames = StringBuilder()
        val selectedIds = selectedLanguageIds.split(",")
        for (i in selectedIds) {
            for (language in languagesList) {
                if (TextUtils.equals(i, language.id.toString())) {
                    selectedLanguageNames.append(language.name + " , ")
                    break
                }
            }
        }
        return selectedLanguageNames.toString().substring(0, selectedLanguageNames.length - 2)
    }

    private fun setGoLiveVisibility(isRegistered: Boolean) {
        val demoLectureData = viewModel.demoLectureDetailsApi.value!!
        if (isRegistered) {
            val shouldShow = checkGoLiveVisibility(demoLectureData.lectureDate, demoLectureData.duration,demoLectureData.dbCTime)
            if (shouldShow) {
                binding.btnGoLive.visibility = View.VISIBLE
            } else {
                binding.btnGoLive.visibility = View.GONE
            }
        } else {
            binding.btnGoLive.visibility = View.GONE
        }
//        binding.btnGoLive.visibility = View.VISIBLE //---remove
    }

    private fun fetchRegisterStatus() {
        val demoLectureId = viewModel.demoLectureDetailsApi.value?.id ?: 0
        val studentId = StorePreferences(this).studentId.toString()
        val demoLectureRegisterStatusRequest = DemoLectureRegisterStatusRequest(demoLectureId, studentId)
        viewModel.fetchDemoLectureRegisterStatus(demoLectureRegisterStatusRequest)
    }

    private fun setRegisterVisibility(isRegistered: Boolean) {
        if (isRegistered) {
            binding.btnAlreadyRegistered.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
        } else {
            binding.btnAlreadyRegistered.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    private fun fetchDemoLectureDetails() {
        viewModel.getDemoLectureDetails()
    }

    private fun setClickListeners() {
        binding.btnGoLive.setOnClickListener {
            val demoLectureDetailsResData = viewModel.demoLectureDetailsApi.value
            val roomName = demoLectureDetailsResData?.roomname ?: "Room Name"
            val participantName = StorePreferences(this).userName
            VideoCallInterfaceImplementation.launchVideoCall(this, roomName, participantName)
        }
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            registerForDemoLecture()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setVideo()
    }

    private fun setVideo() {
//        viewModel.demoLectureDetailsApi.value?.lectureVideo?.let { code->
//            val videoUrl = VIDEO_BASE_URL + code
//            val uri: Uri = Uri.parse(videoUrl)
//            binding.videoView.setVideoURI(uri)
//            val mc = MediaController(this)
//            mc.setAnchorView(binding.videoView)
//            mc.setMediaPlayer(binding.videoView)
//            binding.videoView.setMediaController(mc)
//            binding.videoView.seekTo(100)
//            val lp: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//            lp.gravity = Gravity.BOTTOM
//            mc.layoutParams = lp
//            (mc.parent as ViewGroup).removeView(mc)
//            binding.videoViewWrapper.addView(mc)
//        }

        viewModel.demoLectureDetailsApi.value?.lectureVideo?.let { code ->
            val videoUrl = "$VIDEO_BASE_URL$code"
            val uri: Uri = Uri.parse(videoUrl)
            simpleExoPlayer = SimpleExoPlayer.Builder(MyApplication.context).build()
            exoPlayerView.player = simpleExoPlayer
            val mediaItem = MediaItem.fromUri(uri)
            simpleExoPlayer.setMediaItem(mediaItem)
            simpleExoPlayer.prepare()
            simpleExoPlayer.playWhenReady = true
        }
    }

    private fun registerForDemoLecture() {
        val demoLecture = viewModel.demoLectureDetailsApi.value
        demoLecture?.let {
            val lectureId = it.id
            val trainerId = it.trainerId
            val attendeeId = StorePreferences(this).studentId.toString()
            val demoLectureRegisterRequest = DemoLectureRegisterRequest(lectureId, trainerId, attendeeId)
            viewModel.registerForDemoLecture(demoLectureRegisterRequest)
        }
    }

    companion object {
        val ACTIVE_DEMO_LECTURE = "ACTIVE_DEMO_LECTURE"

        fun launchActivity(activity: Activity, activeDemoLectures: Int) {
            val intent = Intent(activity, DemoLectureDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(ACTIVE_DEMO_LECTURE, activeDemoLectures)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }

        fun checkGoLiveVisibility(guestLectureStartDateString: String?, duration: Int?, currentDBTime: String): Boolean {
            var guestLectureDuration = 0
            if (duration != null) {
                guestLectureDuration = duration
            }
            var isTenMinutesBeforeStart = false
            var isStarted = false
            var isDurationNotCompleted = false
            val inputTimeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            inputTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            val currentDate = Calendar.getInstance()
            val guestLectureStartDateCalendar = Calendar.getInstance()
            guestLectureStartDateString?.let { startTimeNotNull ->
                guestLectureStartDateCalendar.time = inputTimeFormat.parse(startTimeNotNull)
                currentDate.time = inputTimeFormat.parse(currentDBTime)
                val timeDiffInMillis = guestLectureStartDateCalendar.timeInMillis - currentDate.timeInMillis
                val numberOfDatesDiff = currentDate.calculateDateDiff(currentDate)
                if (numberOfDatesDiff == 1L) {
                    if (timeDiffInMillis < 0) {
                        isTenMinutesBeforeStart = timeDiffInMillis <= 10 * 60 * 1000 && timeDiffInMillis > 0
                        isStarted = true
                    }
                }
                if (isStarted) {
                    val durationInMillis = guestLectureDuration * 60 * 1000
                    if (currentDate.timeInMillis <= guestLectureStartDateCalendar.timeInMillis + durationInMillis) {
                        isDurationNotCompleted = true
                    }
                }
            }
            return isTenMinutesBeforeStart || isDurationNotCompleted
        }
    }

    override fun onPause() {
        super.onPause()
        if(::simpleExoPlayer.isInitialized) simpleExoPlayer.release()
    }
}