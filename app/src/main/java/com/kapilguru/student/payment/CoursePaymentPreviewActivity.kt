package com.kapilguru.student.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.MyApplication
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.CourseDetailsActivity
import com.kapilguru.student.databinding.ActivityCoursePaymentPreviewBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.payment.model.BatchDays
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import com.kapilguru.student.payment.model.TransactionStatusResponseData
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.showAppToast

class CoursePaymentPreviewActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {

    private val TAG = "CoursePaymentPreviewActivity"
    lateinit var binding: ActivityCoursePaymentPreviewBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var mInitiateTransactionRequest: InitiateTransactionRequest
    lateinit var paymentStatusActivityContract: ActivityResultContract<TransactionStatusResponseData, Intent>
    private lateinit var paymentStatusActivityLauncher: ActivityResultLauncher<TransactionStatusResponseData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBar()
        getIntentData()
        showData()
        observeViewModelData()
        setClickListeners()
        setPaymentListener(this)
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_payment_preview)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        paymentStatusActivityContract = object : ActivityResultContract<TransactionStatusResponseData, Intent>() {
            override fun createIntent(context: Context, input: TransactionStatusResponseData?): Intent {
                return Intent(context, PaymentStatusActivity::class.java).apply {
                    val bundle = Bundle().apply {
                        putParcelable(PaymentStatusActivity.TRANSACTION_STATUS_RESPONSE_KEY, input)
                    }
                    putExtras(bundle)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent {
                return intent ?: Intent()
            }
        }

        paymentStatusActivityLauncher = registerForActivityResult(paymentStatusActivityContract) {
            Log.d(TAG, "check initLateInitVariables paymentStatusActivityLauncher : ")
            launchCourseActivity()
        }
    }

    private fun setCustomActionBar() {
        binding.actionbar.actvActivityName.text = getString(R.string.payment_preview_activity_name)
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
    }


    private fun launchCourseActivity() {
        Log.d(TAG, "check launchWebinarActivity: ")
        CourseDetailsActivity.reLaunchActivityToCheckRegisterStatus(this)
        finish()
    }

    private fun getIntentData() {
        mInitiateTransactionRequest = intent.getParcelableExtra(CoursePaymentPreviewActivity.INIT_TRANS_REQ)!!
        addTransactionRequestData()
        binding.model = mInitiateTransactionRequest
    }

    private fun addTransactionRequestData() {
        mInitiateTransactionRequest.userCode = StorePreferences(this).userCode
        mInitiateTransactionRequest.userId = StorePreferences(this).studentId
        val userId = mInitiateTransactionRequest.userId
        val orderId = "o_000" + userId + "_" + System.currentTimeMillis().toString()
        mInitiateTransactionRequest.orderId = orderId
        mInitiateTransactionRequest.grandTotal = mInitiateTransactionRequest.amount!! + mInitiateTransactionRequest.getMobilePlatformCharges()!!
    }


    private fun showData() {
        binding.actvNameValue.text = StorePreferences(this).userName
        binding.startDate = intent.getStringExtra(CoursePaymentPreviewActivity.START_DATE)
        binding.endDate = intent.getStringExtra(CoursePaymentPreviewActivity.END_DATE)
        intent.getParcelableExtra<BatchDays>(CoursePaymentPreviewActivity.BATCH_DAYS)?.let {
            binding.batchDays = it
        }
    }

    private fun observeViewModelData() {
        observePackageInitTransactionResponse()
        observePackageTransactionStatusApi()
    }

    private fun setClickListeners() {
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
        binding.btnPay.setOnClickListener {
//            mInitiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
            Log.d(TAG, "setClickListeners: $mInitiateTransactionRequest")
            callInitiateTransactionApi(mInitiateTransactionRequest)
        }
    }


    private fun observePackageInitTransactionResponse() {
        paymentViewModel.initiateTransactionResponse.observe(this, Observer { initTransApiRes ->
            when (initTransApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    parseInitTransApiResponse(initTransApiRes.data?.initiateTransResData!!)
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun observePackageTransactionStatusApi() {
        paymentViewModel.transactionStatusResponse.observe(this, Observer { transStatusApiRes ->
            when (transStatusApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    parseTransactionStatusResponse(transStatusApiRes.data?.transactionStatusResponseData)
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        if (status == PaymentStatus.SUCCESS) {
            val trainerId = intent.getIntExtra(CoursePaymentPreviewActivity.TRAINER_ID, 0)
            callWebinarTransactionStatusApi(trainerId)
        } else {
            showAppToast(this, "Transaction Cancelled")
        }
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponseData: TransactionStatusResponseData) {
        paymentStatusActivityLauncher.launch(transactionStatusResponseData)
    }

    override fun onPaymentFailed(message: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.apply {
            setCancelable(false)
            setPositiveButton(R.string.ok) { dialog, id ->
                finish()
            }
            setMessage(message)
        }
        builder.create().show()
    }


    companion object {
        const val INIT_TRANS_REQ = "INIT_TRANS_REQ"
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"
        const val TRAINER_ID = "TRAINER_ID"
        const val COURSE_ID = "COURSE_ID"
        const val BATCH_DAYS = "BATCH_DAYS"

        fun launchActivity(initiateTransactionRequest: InitiateTransactionRequest, startDate: String, endDate: String, trainerId: Int, courseId: Int, batchDays: BatchDays, activity: Activity) {
            Log.d("TAG", "launchActivity: $initiateTransactionRequest $startDate $endDate $trainerId $courseId $batchDays $activity")
            val intent = Intent(activity, CoursePaymentPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(INIT_TRANS_REQ, initiateTransactionRequest)
            bundle.putString(START_DATE, startDate)
            bundle.putString(END_DATE, endDate)
            bundle.putInt(TRAINER_ID, trainerId)
            bundle.putInt(COURSE_ID, courseId)
            bundle.putParcelable(BATCH_DAYS, batchDays)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}