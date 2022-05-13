package com.kapilguru.student.wallet.referralDetails

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.PARAM_REFERRAL_LIST
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityReferralAmountViewDetailsBinding
import com.kapilguru.student.wallet.earningsDetails.AmountViewModel
import com.kapilguru.student.wallet.model.EarningsReferralList
import kotlinx.android.synthetic.main.activity_amount_view_details.*

class ReferralAmountViewDetails : BaseActivity() {

    lateinit var binding: ActivityReferralAmountViewDetailsBinding
    lateinit var viewModel: AmountViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var referralAmountDetailsRecycler: ReferralAmountDetailsRecycler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_referral_amount_view_details)
        setCustomActionBarListener()
        setRecycler()
        readIntent()
    }

    private fun setRecycler() {
        referralAmountDetailsRecycler = ReferralAmountDetailsRecycler()
        recycler.adapter = referralAmountDetailsRecycler
    }

    private fun readIntent() {
        val data: ArrayList<EarningsReferralList>? = intent.getParcelableArrayListExtra(PARAM_REFERRAL_LIST)
        if (data != null) {
            referralAmountDetailsRecycler.listOFEarningsDetailsReferrals = data
        }
    }
    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar.root, getString(R.string.wallet))
    }

}