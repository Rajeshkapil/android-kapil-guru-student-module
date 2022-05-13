package com.kapilguru.student.webinar.webinarDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.kapilguru.student.*
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.databinding.ActivityWebinarDetailsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.payment.WebinarPaymentPreviewActivity
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.videoCall.VideoCallInterfaceImplementation
import com.kapilguru.student.webinar.model.LanguageData
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResData
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusRequest
import com.kapilguru.student.webinar.webinarDetails.model.WebinarRegisterStatusResData
import com.kapilguru.student.webinar.webinarDetails.viewModel.WebinarDetailsViewModel
import com.kapilguru.student.webinar.webinarDetails.viewModel.WebinarDetailsViewModelFactory
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class WebinarDetailsActivity : AppCompatActivity() {
    private val TAG = "WebinarDetailsActivity"
    lateinit var binding: ActivityWebinarDetailsBinding
    lateinit var viewModel: WebinarDetailsViewModel
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var exoPlayerView :PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar_details)
        viewModel = ViewModelProvider(this, WebinarDetailsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(WebinarDetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        exoPlayerView = binding.exoVideoView
        viewModel.webinarDataIntent = intent?.getIntExtra(WEBINAR_DATA, -1)
        setActivityName()
        observeViewModelData()
        fetchWebinarDetails()
        setClickListeners()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent: ")
        super.onNewIntent(intent)
        intent?.let { intentNotNull ->
            if (intentNotNull.action == ACTION_CHECK_REGISTER_STATUS) {
                fetchRegisterStatus()
            }
        }
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.webinar)
    }

    private fun observeViewModelData() {
        observeRegisterStatusApiResponse()
        observeWebinarDetailsApiResponse()
    }

    private fun observeWebinarDetailsApiResponse() {
        viewModel.webinarDetailsRes.observe(this, Observer { webinarDetailsApiRes ->
            when (webinarDetailsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    webinarDetailsApiRes.data?.let { webinarDetailsResponse ->
                        webinarDetailsResponse.webinarDetailsDataList?.let { webinarResData ->
                            try {
                                viewModel.webinarDetailsApi.value = webinarResData[0]
                                (application as MyApplication).getLanguages(object : MyApplication.FetchLanguagesListener {
                                    override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
                                        GlobalScope.launch {
                                            withContext(Dispatchers.Main) {
                                                setLanguagesText(languagesDataList, webinarResData[0])
                                            }
                                        }
                                    }
                                })
//                                setGoLiveVisibility()
                                fetchRegisterStatus()
                                setVideo()
                            } catch (exception: IndexOutOfBoundsException) {
                                progressDialog.dismissLoadingDialog()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (webinarDetailsApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(this, getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    private fun fetchRegisterStatus() {
        Log.d(TAG, "fetchRegisterStatus: ")
        val webinarId = viewModel.webinarDetailsApi.value?.id ?: 0
        val studentId = StorePreferences(this).studentId.toString()
        val webinarRegisterStatusRequest = WebinarRegisterStatusRequest(webinarId, studentId)
        viewModel.getWebinarRegisterStatus(webinarRegisterStatusRequest)
    }

    private fun observeRegisterStatusApiResponse() {
        viewModel.webinarRegisterStatusApiRes.observe(this, Observer { webinarRegisterStatusApiRes ->
            when (webinarRegisterStatusApiRes.status) {
                Status.LOADING -> {
                    // do nothing as progress dialog is already being shown
                }
                Status.SUCCESS -> {
                    val webinarRegisterStatusResDataList = webinarRegisterStatusApiRes.data?.data
                    onWebinarRegisterStatusResponseReceived(webinarRegisterStatusResDataList)
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun onWebinarRegisterStatusResponseReceived(webinarRegisterStatusResDataList: List<WebinarRegisterStatusResData>?) {
        var webinarRegisterStatusResData: WebinarRegisterStatusResData? = null
        webinarRegisterStatusResDataList?.let {
            if (it.isNotEmpty()) {
                webinarRegisterStatusResData = it[0]
            }
        }
        webinarRegisterStatusResData?.let {
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

    private fun setRegisterVisibility(isRegistered: Boolean) {
        Log.d(TAG, "setRegisterVisibility: isRegistered " + isRegistered)
        if (isRegistered) {
            binding.btnAlreadyRegistered.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
        } else {
            binding.btnAlreadyRegistered.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
        }
    }

    private fun setLanguagesText(languagesList: ArrayList<LanguageData>, webinarDetailsResData: WebinarDetailsResData) {
        CoroutineScope(Dispatchers.Main).launch {
            val stringToShow = getSelectedLanguagesStringToShow(languagesList, webinarDetailsResData.languages.fromBase64())
            binding.actvLanguagesValue.text = stringToShow
        }
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

    private fun fetchWebinarDetails() {
        viewModel.getWebinarDetailsResponse()
    }

    private fun setGoLiveVisibility(webinarRegisterStatusResDataList: Boolean) {
        val webinarData = viewModel.webinarDetailsApi.value!!
        if (webinarRegisterStatusResDataList) {
            val shouldShow = checkAndSetGoLiveVisibility(webinarData.startDate, webinarData.endDate, webinarData.dbCTime)
            if (shouldShow) {
                binding.btnGoLive.visibility = View.VISIBLE
            } else {
                binding.btnGoLive.visibility = View.GONE
            }
        } else {
            binding.btnGoLive.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnGoLive.setOnClickListener {
            val webinarDetailsResData = viewModel.webinarDetailsApi.value
            val roomName = webinarDetailsResData?.roomname ?: "Room Name"
            val participantName = StorePreferences(this).userName
            VideoCallInterfaceImplementation.launchVideoCall(this, roomName, participantName)
        }
        binding.btnRegister.setOnClickListener {
            val initiateTransactionRequest = InitiateTransactionRequest()
            val webinarDetailsResData = viewModel.webinarDetailsApi.value
            initiateTransactionRequest.amount = webinarDetailsResData?.price?.toDouble()
            initiateTransactionRequest.productType = PAYMENT_PRODUCT_TYPE_WEBINAR
            initiateTransactionRequest.productId = webinarDetailsResData?.id
            initiateTransactionRequest.productCode = webinarDetailsResData?.code
            val startDate = webinarDetailsResData?.startDate ?: ""
            val endDate = webinarDetailsResData?.endDate ?: ""
            val trainerId = viewModel.webinarDetailsApi.value?.trainerId ?: 0
            val webinarId = viewModel.webinarDetailsApi.value?.id ?: 0
            WebinarPaymentPreviewActivity.launchActivity(initiateTransactionRequest, startDate, endDate, trainerId, webinarId, this)
        }
    }

    private fun setVideo() {
//        viewModel.webinarDetailsApi.value?.webinarVideo?.let { code->
//            val videoUrl = "$VIDEO_BASE_URL$code"
//            val uri: Uri = Uri.parse(videoUrl)
//            binding.videoView.setVideoURI(uri)
//            val mediaController = MediaController(this)
//            mediaController.setAnchorView(binding.videoView)
//            mediaController.setMediaPlayer(binding.videoView)
//            binding.videoView.setMediaController(mediaController)
//            binding.videoView.seekTo(100)
//        }

//        viewModel.webinarDetailsApi.value?.webinarVideo?.let { code->
//            val videoUrl = "$VIDEO_BASE_URL$code"
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

        viewModel.webinarDetailsApi.value?.webinarVideo?.let { code ->
            val videoUrl = "$VIDEO_BASE_URL$code"
            val uri: Uri = Uri.parse(videoUrl)
             simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
            exoPlayerView.player = simpleExoPlayer
            val mediaItem = MediaItem.fromUri(uri)
            simpleExoPlayer.setMediaItem(mediaItem)
            simpleExoPlayer.prepare()
            simpleExoPlayer.playWhenReady = true
        }
    }

    override fun onRestart() {
        super.onRestart()
        setVideo()
    }

    override fun onStop() {
        super.onStop()
        if(::simpleExoPlayer.isInitialized) simpleExoPlayer.release()
    }

    companion object {
        const val TAG = "WebinarDetailsActivity"
        const val WEBINAR_DATA = "WEBINAR_DATA"
        const val WEBINAR_ID = "WEBINAR_ID"
        const val ACTION_CHECK_REGISTER_STATUS = "ACTION_CHECK_REGISTER_STATUS"

        fun launchActivity(activity: Activity, activeWebinar: Int) {
            val intent = Intent(activity, WebinarDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(WEBINAR_DATA, activeWebinar)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }

        fun reLaunchActivityToCheckRegisterStatus(context: Context) {
            val intent = Intent(context, WebinarDetailsActivity::class.java)
            intent.action = ACTION_CHECK_REGISTER_STATUS
            context.startActivity(intent)
        }

        fun checkAndSetGoLiveVisibility(webinarStartDateString: String?, webinarEndDateString: String?, currentDBTime:String): Boolean {
            var isTenMinutesBeforeStart = false
            var isStarted = false
            var isDurationNotCompleted = false
            val inputTimeFormat = SimpleDateFormat(API_FORMAT_DATE_AND_TIME_WITH_T)
            inputTimeFormat.timeZone = TimeZone.getTimeZone("GMT")
            val currentDate = Calendar.getInstance()
            val webinarStartDateCalendar = Calendar.getInstance()
            val webinarEndDateCalendar = Calendar.getInstance()
            webinarStartDateString?.let { startTimeNotNull ->
                webinarStartDateCalendar.time = inputTimeFormat.parse(startTimeNotNull)
                currentDate.time = inputTimeFormat.parse(currentDBTime)
             /*   webinarStartDateCalendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                webinarStartDateCalendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                webinarStartDateCalendar.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))*/
                val timeDiffInMillis = webinarStartDateCalendar.timeInMillis - currentDate.timeInMillis
                val numberOfDatesDiff = webinarStartDateCalendar.calculateDateDiff(currentDate)
                if (numberOfDatesDiff == 1L) {
                    isTenMinutesBeforeStart = timeDiffInMillis <= 10 * 60 * 1000 && timeDiffInMillis > 0
                    if (timeDiffInMillis < 0) {
                        isStarted = true
                    }
                }
            }
            if (isStarted) {
                webinarEndDateString?.let { endDateNotNull ->
                    webinarEndDateCalendar.time = inputTimeFormat.parse(endDateNotNull)
                    webinarEndDateCalendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
                    webinarEndDateCalendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
                    webinarEndDateCalendar.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))
                    isDurationNotCompleted = webinarEndDateCalendar.timeInMillis >= currentDate.timeInMillis
                }
            }
            return isTenMinutesBeforeStart || isDurationNotCompleted
        }
    }
}