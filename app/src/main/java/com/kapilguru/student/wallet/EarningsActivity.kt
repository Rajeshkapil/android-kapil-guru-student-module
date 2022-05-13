package com.kapilguru.student.wallet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityEarningsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.wallet.earningsDetails.AmountViewDetails
import com.kapilguru.student.wallet.history.EarningsHistoryActivity
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import com.kapilguru.student.wallet.model.EarningsSummary
import com.kapilguru.student.wallet.referralDetails.ReferralAmountViewDetails
import com.kapilguru.student.wallet.requestAmount.RequestAmount
import kotlinx.android.synthetic.main.activity_earnings.*
import java.lang.reflect.Type


class EarningsActivity : BaseActivity(), EarningsMergerView.ItemClickListener{

    lateinit var earningsBinding: ActivityEarningsBinding
    lateinit var earningsViewModel: EarningsViewModel
    lateinit var dialog: CustomProgressDialog
    private  val TAG = "EarningsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        earningsBinding = DataBindingUtil.setContentView(this, R.layout.activity_earnings)
        earningsBinding.clickListener = this

        setCustomActionBarListener()

        earningsViewModel = ViewModelProvider(
            this,
            EarningsViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                application)
        ).get(EarningsViewModel::class.java)
        dialog = CustomProgressDialog(this)
        earningsBinding.earningsModel = earningsViewModel
        earningsBinding.lifecycleOwner = this

        setClickListeners()
        observeViewModelData()
    }

    private fun setClickListeners() {
        availableAmountView.setClickListener(this)

        history.setOnClickListener {
            navigateToHistory()
        }
    }

    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, earningsBinding.actionbar.root, getString(R.string.wallet))
    }

    private fun observeViewModelData() {
        earningsViewModel.earnings.observe(this, Observer {it->
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    it.data?.let { earnings ->
                        earningsViewModel.earningsInfo.value = earnings.earningsInfo
                       val rawSummary: String? = earnings.earningsInfo?.summary
                        var gson = Gson()
                        rawSummary?.let {
                            val summaryType: Type =
                                object : TypeToken<EarningsSummary?>() {}.type
                            val summary: EarningsSummary = gson.fromJson(rawSummary.toString(), summaryType)
                            summary.let { summary ->
                                earningsViewModel.earningsSummary.value = summary
                            }
                        }

                        val rawEarningsRefundList: String? = earnings.earningsInfo?.refunds
                        gson = Gson()
                        rawEarningsRefundList?.let {
                            val summaryType: Type =
                                object : TypeToken<List<EarningsRefundList?>>() {}.type
                            val earningsRefundList: List<EarningsRefundList> = gson.fromJson(rawEarningsRefundList.toString(), summaryType)
                            earningsRefundList.let { earningsRefundList ->
                                earningsViewModel.earningsRefundList.value = earningsRefundList as ArrayList<EarningsRefundList>
                            }
                        }


                        val rawEarningsReferralList: String? = earnings.earningsInfo?.referrals
                        gson = Gson()
                        rawEarningsReferralList?.let {
                            val summaryType: Type =
                                object : TypeToken<List<EarningsReferralList?>>() {}.type
                            val earningsReferralList: List<EarningsReferralList> = gson.fromJson(rawEarningsReferralList.toString(), summaryType)
                            earningsReferralList.let { earningsReferralList ->
                                earningsViewModel.earningsReferralList.value = earningsReferralList as ArrayList<EarningsReferralList>
                            }
                        }

                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    if (it.data?.status != 200) {
                        it.message?.let { it1 -> showErrorDialog(it1) }
                    } else {
                        showErrorDialog(getString(R.string.try_again))
                    }
                }
            }
        })
    }

    private fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        setCancelable(true)
                    })

                setMessage(message)

            }

            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onCourseClick(view: View, position:Int) {
//        val intent = Intent(view.context, EarningsDetailsActivity::class.java)
//            intent.putExtra(VIEW_SELECTED_LOCKED_AMOUNT,position)
//            startActivity(intent)
    }


    private fun navigateToHistory() {
        startActivity(Intent(this, EarningsHistoryActivity::class.java))
    }

    override fun onItemClick(amountType: AmountType) {

        when(amountType) {
            AmountType.REFUNDAMOUNTAVAILABLE -> {
               startActivity(Intent(this, AmountViewDetails::class.java).putExtra(PARAM_REFUND_LIST,earningsViewModel.earningsRefundList.value))
            }
            AmountType.REFERRALAMOUNTAVAILABLE -> {
                startActivity(Intent(this, ReferralAmountViewDetails::class.java).putExtra(PARAM_REFERRAL_LIST,earningsViewModel.earningsReferralList.value))
            }
            AmountType.PRIZESAMOUNTAVAILABLE -> {
//                startActivity(Intent(this, ReferralAmountDetails::class.java).putExtra(PARAM_IS_AVAILABLE,true))
            }
            AmountType.REQUESTAMOUNT -> {
                val pref = StorePreferences(application)
               var studentId = pref.isBankUpdated
                // future check for bank Details may be
                earningsViewModel.earningsSummary.value?.let {
                    if(it.prizesAmountAvailable!=null || it.prizesAmountAvailable!=null ||
                        it.prizesAmountAvailable!=null) {
                        startActivity(
                            Intent(this, RequestAmount::class.java).putExtra(PARAM_SUMMARY, earningsViewModel.earningsSummary.value)
                                .putExtra(PARAM_API_REQUEST_REFUND_DATA, earningsViewModel.earningsRefundList.value)
                                .putExtra(PARAM_API_REQUEST_REFERRAL_DATA, earningsViewModel.earningsReferralList.value)
                        )
                    }
                }

            }

        }

    }

}