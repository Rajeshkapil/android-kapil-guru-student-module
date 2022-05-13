package com.kapilguru.student.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.payment.model.*
import com.kapilguru.student.payment.viewModel.PaymentViewModel
import com.kapilguru.student.payment.viewModel.PaymentViewModelFactory
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

open class PaymentActivity : AppCompatActivity() {
    private val PAYMENT_TAG = "PaymentActivity"
    lateinit var paymentViewModel: PaymentViewModel
    var mListener: PaymentStatusListener? = null
    val paytmRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentViewModel = ViewModelProvider(this, PaymentViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(PaymentViewModel::class.java)
    }

    fun setPaymentListener(listener: PaymentStatusListener) {
        mListener = listener
    }

    /*@param - oldExpiryDate is the expiry date of existing subscription.
    * can be empty if user is not having any subscription.*/
    fun getExpiryDate(oldExpiryDate: String?): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        if (oldExpiryDate == null) {
            calendar.time = format.parse(format.format(calendar.time))
        } else {
            calendar.time = format.parse(oldExpiryDate)
        }
        calendar.add(Calendar.DAY_OF_YEAR, 365)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy")
        val toReturn = outputFormat.format(calendar.time)
        Log.d(PAYMENT_TAG, "getExpiryDate : toReturn : " + toReturn)
        return toReturn
    }

    /*Initiate transaction Api Request is same for all subscriptions*/
    fun callInitiateTransactionApi(initiateTransactionRequest: InitiateTransactionRequest) {
        paymentViewModel.callInitiateTransactionApi(initiateTransactionRequest)
    }

    /*Separate methods are written for transaction status api as the request body differs from each other*/
    fun callWebinarTransactionStatusApi(trainerId: Int) {
        val transactionStatusRequest = getTransactionStatusRequest(trainerId)
        paymentViewModel.callTransactionStatusApi(transactionStatusRequest)
    }

    private fun getTransactionStatusRequest(trainerId: Int): TransactionStatusRequest {
        val initiateTransactionRequest = paymentViewModel.mInitiateTransactionRequest
        val transactionStatusRequest = TransactionStatusRequest()
        transactionStatusRequest.amount = initiateTransactionRequest.grandTotal
        transactionStatusRequest.orderId = initiateTransactionRequest.orderId
        transactionStatusRequest.productId = initiateTransactionRequest.productId
        transactionStatusRequest.productType = initiateTransactionRequest.productType
        transactionStatusRequest.status = "Registered"
        transactionStatusRequest.trainerId = trainerId
        transactionStatusRequest.userId = initiateTransactionRequest.userId
        initiateTransactionRequest.courseId?.let {
            transactionStatusRequest.courseId = it
        }
        return transactionStatusRequest
    }

    private fun showProgressBar() {
//        paymentProgressDialog.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
//        paymentProgressDialog.visibility = View.GONE
    }

    fun parseInitTransApiResponse(initTransResString: String) {
        val initTransResData = InitiateTransactionResData().getObject(initTransResString)
        if (initTransResData.body?.resultInfo?.resultStatus == "S") {
            openPaytm(initTransResData.body!!.txnToken)
        } else {
            mListener?.onPaymentFailed("Your Payment has failed. Please Try again later")
        }
    }

    fun parseTransactionStatusResponse(transactionStatusResData: TransactionStatusResponseData?) {
        Log.d("TAG", "parseTransactionStatusResponse: " + transactionStatusResData.toString())
        transactionStatusResData?.let {
            if (it.body?.resultInfo?.resultStatus == TRANSACTION_SUCCESS) {
                informTransactionStatusResponse(it)
            }
        } ?: run {
            Log.e(PAYMENT_TAG, "parseTransactionStatusResponse is null")
        }
    }

    private fun openPaytm(txnToken: String) {
        val host = if (BuildConfig.IS_RELEASE_BUILD) "https://securegw.paytm.in/" else "https://securegw-stage.paytm.in/"
        val orderId = paymentViewModel.mInitiateTransactionRequest.orderId
        val mid = if(BuildConfig.IS_RELEASE_BUILD) "KapilG03335944902501" else "KapilG71666698302198"
        val amount = paymentViewModel.mInitiateTransactionRequest.grandTotal.toString()
        val callbackUrl = host + PAYTM_CALLBACK_URL + orderId
        val paytmOrder = PaytmOrder(orderId, mid, txnToken, amount, callbackUrl)

        val transactionManager = TransactionManager(
            paytmOrder, object : PaytmPaymentTransactionCallback {
                override fun onTransactionResponse(p0: Bundle?) {

                }

                override fun networkNotAvailable() {
                    showAppToast(applicationContext, "No Network available")
                }

                override fun onErrorProceed(p0: String?) {
                    showAppToast(applicationContext, "onErrorProceed : $p0")
                }

                override fun clientAuthenticationFailed(p0: String?) {
                    showAppToast(applicationContext, "clientAuthenticationFailed")
                }

                override fun someUIErrorOccurred(p0: String?) {
                    showAppToast(applicationContext, "UI Error Occurred")
                }

                override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
                    showAppToast(applicationContext, "Error loading Web Page")
                }

                override fun onBackPressedCancelTransaction() {
                    showAppToast(applicationContext, "Transaction Cancelled due to back press")
                }

                override fun onTransactionCancel(p0: String?, p1: Bundle?) {
                    showAppToast(applicationContext, "Transaction ")
                }

            }
        )
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, paytmRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == paytmRequestCode) {
            val bundle = data?.extras
            bundle?.let {
                if (resultCode == RESULT_OK) {
                    val jsonObject = JSONObject(it.getString("response")!!)
                    if (jsonObject.optString(getString(R.string.payment_status)) == getString(R.string.payment_txn_success)) {
                        val paymentStatus = jsonObject.getString(getString(R.string.payment_status))
                        if (paymentStatus == getString(R.string.payment_txn_success)) {
                            informOnActivityResult(PaymentStatus.SUCCESS)
                        } else {
                            informOnActivityResult(PaymentStatus.FAILURE)
                        }
                    }
                } else {
                    informOnActivityResult(PaymentStatus.FAILURE)
                }
            }
        }
    }

    private fun informOnActivityResult(status: PaymentStatus) {
        mListener?.onPaymentActivityResult(status)
    }

    private fun informTransactionStatusResponse(transactionStatusResDataData: TransactionStatusResponseData) {
        mListener?.onPaymentTransactionStatusResult(transactionStatusResDataData)
    }

    interface PaymentStatusListener {
        fun onPaymentActivityResult(status: PaymentStatus)
        fun onPaymentTransactionStatusResult(transactionStatusResponseData: TransactionStatusResponseData)
        fun onPaymentFailed(message: String)
    }

    enum class PaymentStatus {
        SUCCESS, FAILURE
    }
}