 package com.kapilguru.student.referandearn.myReferrals

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityMyReferalsListBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.referandearn.myReferrals.model.MyReferralResData
import com.kapilguru.student.referandearn.myReferrals.viewModel.MyReferralViewModel
import com.kapilguru.trainer.referandearn.myReferrals.MyReferralsListAdapter
import com.kapilguru.trainer.referandearn.myReferrals.viewModel.MyReferralViewModelFactory

class MyReferralsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyReferalsListBinding
    lateinit var viewModel: MyReferralViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: MyReferralsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        observeViewModelData()
        getMyReferrals()
        setClickListeners()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_referals_list)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, MyReferralViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))).get(MyReferralViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
        adapter = MyReferralsListAdapter()
        binding.rvMyReferrals.adapter = adapter
    }

    private fun setActivityName() {
        binding.actionbar.actvActivityName.text = getString(R.string.my_referrals)
    }

    private fun observeViewModelData() {
        viewModel.myReferralsApiResponse.observe(this, Observer { myRefApiRes ->
            when (myRefApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    myRefApiRes.data?.myReferralsList?.let { myReferralsList ->
                        checkAndSetAdapterData(myReferralsList)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.showLoadingDialog()
                    when(myRefApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun checkAndSetAdapterData(myReferralsList: ArrayList<MyReferralResData>) {
        if (myReferralsList.isEmpty()) {
            binding.actvNoMyReferrals.visibility = View.VISIBLE
            binding.rvMyReferrals.visibility = View.GONE
            binding.actvInfo.visibility = View.GONE
        } else {
            adapter.setData(myReferralsList)
        }
    }

    private fun getMyReferrals() {
        val studentId = StorePreferences(this).studentId.toString()
        viewModel.getMyReferrals(studentId)
    }

    private fun setClickListeners(){
        binding.actionbar.acivBack.setOnClickListener {
            finish()
        }
    }
}