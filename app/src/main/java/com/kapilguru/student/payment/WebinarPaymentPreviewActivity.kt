package com.kapilguru.student.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.MyApplication.Companion.context
import com.kapilguru.student.databinding.ActivityWebinarPaymentPreviewBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import com.kapilguru.student.payment.model.TransactionStatusResponseData
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity

class WebinarPaymentPreviewActivity : PaymentActivity(), PaymentActivity.PaymentStatusListener {
    private val TAG = "WebinarPaymentPreviewActivity"
    lateinit var binding: ActivityWebinarPaymentPreviewBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var mInitiateTransactionRequest: InitiateTransactionRequest
    lateinit var paymentStatusActivityContract: ActivityResultContract<TransactionStatusResponseData, Intent>
    private lateinit var paymentStatusActivityLauncher: ActivityResultLauncher<TransactionStatusResponseData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        getIntentData()
        showData()
        observeViewModelData()
        setClickListeners()
        setPaymentListener(this)
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar_payment_preview)
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(this)
        binding.actionbar.actvActivityName.text = getString(R.string.payment_preview_activity_name)

        paymentStatusActivityContract = object : ActivityResultContract<TransactionStatusResponseData, Intent>() {
            override fun createIntent(context: Context, input: TransactionStatusResponseData?): Intent {
                Log.d(TAG, "check createIntent: ")
                return Intent(context, PaymentStatusActivity::class.java).apply {
                    val bundle = Bundle().apply {
                        putParcelable(PaymentStatusActivity.TRANSACTION_STATUS_RESPONSE_KEY, input)
                    }
                    putExtras(bundle)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Intent {
                Log.d(TAG, "check parseResult: ")
                return intent ?: Intent()
            }
        }

        paymentStatusActivityLauncher = registerForActivityResult(paymentStatusActivityContract) {
            Log.d(TAG, "check initLateInitVariables paymentStatusActivityLauncher : ")
            launchWebinarActivity()
        }
    }

    private fun launchWebinarActivity() {
        Log.d(TAG, "check launchWebinarActivity: ")
        WebinarDetailsActivity.reLaunchActivityToCheckRegisterStatus(this)
        finish()
    }

    private fun getIntentData() {
        mInitiateTransactionRequest = intent.getParcelableExtra(INIT_TRANS_REQ)!!
        addTransactionRequestData()
        binding.model = mInitiateTransactionRequest
    }

    private fun showData() {
        binding.actvNameValue.text = StorePreferences(this).userName
        binding.startDate = intent.getStringExtra(START_DATE)
        binding.endDate = intent.getStringExtra(END_DATE)
    }

    private fun observeViewModelData() {
        observePackageInitTransactionResponse()
        observePackageTransactionStatusApi()
    }

    private fun addTransactionRequestData() {
        mInitiateTransactionRequest.userCode = StorePreferences(this).userCode
        mInitiateTransactionRequest.userId = StorePreferences(this).studentId
        val userId = mInitiateTransactionRequest.userId
        val orderId = "O_000" + userId + "_" + System.currentTimeMillis().toString()
        mInitiateTransactionRequest.orderId = orderId
        mInitiateTransactionRequest.grandTotal = mInitiateTransactionRequest.amount!! + mInitiateTransactionRequest.getMobilePlatformCharges()!!
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
                    when(initTransApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
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
                    when(transStatusApiRes.code) {
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
        binding.btnPay.setOnClickListener {
            mInitiateTransactionRequest.grandTotal = binding.actvGrandTotalValue.text.toString().toDouble()
            callInitiateTransactionApi(mInitiateTransactionRequest)
        }
    }

    override fun onPaymentActivityResult(status: PaymentStatus) {
        if (status == PaymentStatus.SUCCESS) {
            val trainerId = intent.getIntExtra(TRAINER_ID, 0)
            callWebinarTransactionStatusApi(trainerId)
        } else {
            showAppToast(this, "Transaction Cancelled")
        }
    }

    override fun onPaymentTransactionStatusResult(transactionStatusResponseData: TransactionStatusResponseData) {
        Log.d(TAG, "check launching onPaymentTransactionStatusResult: ")
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
        const val WEBINAR_ID = "WEBINAR_ID"

        fun launchActivity(initiateTransactionRequest: InitiateTransactionRequest, startDate: String, endDate: String, trainerId: Int, webinarId: Int, activity: Activity) {
            val intent = Intent(activity, WebinarPaymentPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(INIT_TRANS_REQ, initiateTransactionRequest)
            bundle.putString(START_DATE, startDate)
            bundle.putString(END_DATE, endDate)
            bundle.putInt(TRAINER_ID, trainerId)
            bundle.putInt(WEBINAR_ID, webinarId)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}