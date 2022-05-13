package com.kapilguru.student.wallet.history

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityEarningsHistoryBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.wallet.history.model.PaidBankDetails
import com.kapilguru.student.wallet.history.viewModels.EarningsHistoryViewModel
import com.kapilguru.student.wallet.history.viewModels.EarningsHistoryViewModelFactory
import com.kapilguru.student.wallet.model.EarningsReferralList
import com.kapilguru.student.wallet.model.EarningsRefundList
import org.json.JSONArray

class EarningsHistoryActivity : BaseActivity(),
    EarningsDetailsHistoryAdapter.EarningsDetailsHistoryAdapterListener {

    lateinit var binding: ActivityEarningsHistoryBinding
    lateinit var viewModel: EarningsHistoryViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var adapter: EarningsDetailsHistoryAdapter
    private  val TAG = "EarningsHistoryActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R .layout.activity_earnings_history)
        setCustomActionBar()

        viewModel = ViewModelProvider(
                this, EarningsHistoryViewModelFactory(
                    ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),
                    application)
        ).get(EarningsHistoryViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)

        adapter = EarningsDetailsHistoryAdapter(this,this )
        binding.recyclerview.adapter = adapter
        viewModel.fetchResponse(StorePreferences(this).studentId.toString())
        viewModelsObservers()

    }

    fun setCustomActionBar() {
        setActionbarBackListener(this, binding.actionbar.root, getString(R.string.wallet))
    }

    private fun viewModelsObservers() {
        viewModel.resultInfo.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let { earningsListApi ->
//                        Log.d(TAG, "viewModelsObservers: ${earningsListApi.earningsHistoryResponse?.size}")
                        adapter.setData(earningsListApi.earningsHistoryResponse)
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
//                    if (response.data?.status != 200) {
////                        it.message?.let { it1 -> showErrorDialog(it1) }
//                    } else {
////                        showErrorDialog(getString(R.string.try_again))
//                    }
                    when (response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })

        viewModel.paymentInfo.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.let { paymentAmountInfo ->
                        val paymentInfo: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.payment
                        adapter.paidBankDetails.value = PaidBankDetails().getPaidBankDetails(paymentInfo)
                        // set Courses
                        val refundInfo: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.refunds
                        refundInfo?.let {
                            val refundArray = JSONArray(refundInfo)
                            val earningsRefundList = arrayListOf<EarningsRefundList>()
                            for (i in 0 until refundArray.length()) {
                                val jsonstr = refundArray.getJSONObject(i)
                                earningsRefundList.add(EarningsRefundList().getEarningsRefundList(jsonstr))
                            }
                            viewModel.earningsRefund = earningsRefundList
                        }


                        // set referrals
                        val referrals: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.referrals
                        referrals?.let {
                            val referralsArray = JSONArray(referrals)
                            val referralAmountDetailsList = arrayListOf<EarningsReferralList>()
                            for (i in 0 until referralsArray.length()) {
                                val jsonstr = referralsArray.getJSONObject(i)
                                referralAmountDetailsList.add(EarningsReferralList().getEarningsReferralList(jsonstr))
                            }
                            viewModel.earningsReferral = referralAmountDetailsList
                        }


                        // set webinar
                      /*  val webinars: String? = paymentAmountInfo.historyPaymentAmountDetailsResponse.webinars
                        webinars?.let {
                            val webinarsArray = JSONArray(webinars)
                            val webinarAmountDetailsList = arrayListOf<WebinarAmountResponse>()
                            for (i in 0 until webinarsArray.length()) {
                                val jsonstr = webinarsArray.getJSONObject(i)
                                webinarAmountDetailsList.add(WebinarAmountResponse().getWebinarAmountDetails(jsonstr))
                            }
                            viewModel.webinarAmountDetails = webinarAmountDetailsList
                        }
*/

                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
//                    if (response.data?.status != 200) {
////                        it.message?.let { it1 -> showErrorDialog(it1) }
//                    } else {
////                        showErrorDialog(getString(R.string.try_again))
//                    }
                }
            }
        })

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

    override fun getSelectedId(id: String) {
        val pref = StorePreferences(application)
        viewModel.fetchHistoryDetailsAmount(trainerId =  pref.studentId.toString(),selectedId = id)
    }

    override fun onCourseAmountClick() {
//        startActivity(Intent(this, AmountViewDetails::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.earningsDetailsCourse))
    }

    override fun onReferralAmountClick() {
//        startActivity(Intent(this, ReferralAmountDetails::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.referralAmountDetails))
    }

    override fun onWebinarAmountClick() {
//        startActivity(Intent(this, WebinarAmount::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_EARNINGS_HISTORY).putParcelableArrayListExtra(PARAM_AMOUNT_LIST_INFO,viewModel.webinarAmountDetails))
    }
}