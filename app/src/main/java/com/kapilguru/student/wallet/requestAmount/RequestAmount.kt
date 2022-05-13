package com.kapilguru.student.wallet.requestAmount

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityRequestAmountBinding
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.wallet.history.EarningsHistoryActivity
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import com.kapilguru.student.wallet.model.EarningsSummary

class RequestAmount : BaseActivity() {

    lateinit var binding : ActivityRequestAmountBinding
    lateinit var viewModel : RequestAmountViewModel
    private  val TAG = "RequestAmount"
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_amount)
        viewModel = ViewModelProvider(this, RequestAmountViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(RequestAmountViewModel::class.java)
        binding.model = viewModel
        dialog = CustomProgressDialog(this)
        binding.lifecycleOwner = this
        setCustomActionBarListener()
        getIntentData()
        clickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.requestMoneyCommonResponseApi.observe(this, Observer {
            when(it.status) {
                Status.LOADING -> dialog.showLoadingDialog()
                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    showSingleButtonErrorDialog("Successfully submitted, Amount Will be reflected with in 3-5 Banking Business Days ")
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    showSingleButtonErrorDialog("Some thing went wrong, Please try after some time")
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun getIntentData() {
        val data: EarningsSummary? = intent.getParcelableExtra(PARAM_SUMMARY)
        if (data != null) {
            viewModel.earningsSummary.value = data
        }
        val earningsRefundList = intent.getParcelableArrayListExtra<EarningsRefundList>(PARAM_API_REQUEST_REFUND_DATA)
        if (data != null) {
            viewModel.earningsRefundList.value = earningsRefundList
        }
        val earningsReferralList = intent.getParcelableArrayListExtra<EarningsReferralList>(PARAM_API_REQUEST_REFERRAL_DATA)
        if (data != null) {
            viewModel.earningsReferralList.value = earningsReferralList
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.request_money))
    }


    private fun clickListeners() {
        binding.referralCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                setReferralVisibility(true)
            } else {
                setReferralVisibility(false)
            }
        }
        binding.refundCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                setRefundVisibility(true)
            } else {
                setRefundVisibility(false)
            }
        }

        setPriceVisibility()


        binding.btnRequestMoney.setOnClickListener {
            if (hasWallet()) {
                viewModel.constructData()
            }
        }
    }

    private fun hasWallet(): Boolean {
        return true
    }


    fun setReferralVisibility(isVisible: Boolean) {
        if(isVisible) {
            binding.refundId.visibility = View.VISIBLE
            viewModel.isReferralAdded.value = true
        } else {
            binding.refundId.visibility = View.GONE
            viewModel.isReferralAdded.value = false
        }
    }

    fun setRefundVisibility(isVisible: Boolean) {
        if(isVisible) {
            binding.referralId.visibility = View.VISIBLE
            viewModel.isRefundAdded.value = true
        } else {
            binding.referralId.visibility = View.GONE
            viewModel.isRefundAdded.value = false
        }
    }

    fun setPriceVisibility() {
        binding.priceId.visibility = View.GONE
    }


    fun showSingleButtonErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { dialog, id ->
                    setCancelable(true)
                    navigateToHistory()
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog.show()
    }

    private fun navigateToHistory() {
        startActivity(Intent(this, EarningsHistoryActivity::class.java))
        finish()
    }

}