package com.kapilguru.student.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityPaymentStatusBinding
import com.kapilguru.student.payment.model.TransactionStatusResponseData

class PaymentStatusActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentStatusBinding
    private lateinit var mTransactionStatusResponseData: TransactionStatusResponseData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLateInitVariables()
        getIntentData()
        setBindingData()
        setActivityName()
        setClickListeners()
    }

    private fun initializeLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_status)
        binding.lifecycleOwner = this
    }

    private fun getIntentData() {
        mTransactionStatusResponseData = intent.getParcelableExtra(TRANSACTION_STATUS_RESPONSE_KEY)!!
        binding.model = mTransactionStatusResponseData
    }

    private fun setBindingData() {
    }

    private fun setActivityName() {
        binding.actionbar.actvActivityName.text = getString(R.string.payment_status_activity)
    }

    private fun setClickListeners() {
        binding.btnDone.setOnClickListener {
            finish()
        }
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TRANSACTION_STATUS_RESPONSE_KEY = "TRANSACTION_STATUS_RESPONSE"

        fun launchActivity(transactionStatusResponseData: TransactionStatusResponseData, activity: Activity) {
            val intent = Intent(activity, PaymentStatusActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(TRANSACTION_STATUS_RESPONSE_KEY, transactionStatusResponseData)
                }
                putExtras(bundle)
            }
            activity.startActivity(intent)
        }
    }
}
