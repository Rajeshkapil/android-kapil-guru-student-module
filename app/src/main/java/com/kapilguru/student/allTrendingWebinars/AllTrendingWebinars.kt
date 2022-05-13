package com.kapilguru.student.allTrendingWebinars

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.allTrendingWebinars.viewModel.AllTrendingWebinarsViewModel
import com.kapilguru.student.allTrendingWebinars.viewModel.AllTrendingWebinarsViewModelFactory

import com.kapilguru.student.databinding.ActivityAllTrendingWebinarsBinding
import com.kapilguru.student.homeActivity.dashboard.tabFragments.TrendingWebinarAdapter
import com.kapilguru.student.homeActivity.models.AllWebinarsApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.model.LanguageData
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity


class AllTrendingWebinars : BaseActivity(), TrendingWebinarAdapter.TrendingWebinarCardClick {
    private val TAG = "AllTrendingWebinars"
    lateinit var binding: ActivityAllTrendingWebinarsBinding
    lateinit var viewModel: AllTrendingWebinarsViewModel
    lateinit var progressDialog: CustomProgressDialog
    private var allwebinar: ArrayList<AllWebinarsApi>? = null
    lateinit var trendingWebinars: TrendingWebinarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_trending_webinars)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, AllTrendingWebinarsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllTrendingWebinarsViewModel::class.java)
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.webinars))
        setAdapter()
        fetchFromIntent()
    }

    private fun setAdapter() {
        trendingWebinars = TrendingWebinarAdapter(this, true)
        binding.trendingAllWebinarsRecy.adapter = trendingWebinars
    }

    private fun fetchWebinars() {
        viewModel.fetchAllWebinars()
    }

    private fun fetchFromIntent() {
        allwebinar = intent.getParcelableArrayListExtra<AllWebinarsApi>(PARAM_ALL_TRENDING_WEBINARS_LIST)
        if (allwebinar == null) {
            allwebinar?.let { it ->
                showTrendingRecycler(it)
            } ?: kotlin.run {
                observeViewModelData()
                fetchWebinars()
            }
        }
    }

    private fun observeViewModelData() {
        viewModel.allWebinarsData.observe(this, Observer { response ->
            when (response.status) {

                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data.let { allWebinars ->
                        showTrendingRecycler(allWebinars as ArrayList<AllWebinarsApi>)
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (response.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(this, getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    private fun showTrendingRecycler(allWebinars: ArrayList<AllWebinarsApi>?) {
        allWebinars?.let { allWebinarsNotNull ->
            if (allWebinarsNotNull.isEmpty()) {
                showEmptyView(true)
            } else {
                showEmptyView(false)
                setAdapterData(allWebinarsNotNull)
            }
        } ?: kotlin.run {
            showEmptyView(true)
        }
    }

    private fun setAdapterData(allWebinars: ArrayList<AllWebinarsApi>) {
        val allDemoLecturesWithLanguagesText = addLanguages(allWebinars)
        trendingWebinars.upComingScheduleApiList = allDemoLecturesWithLanguagesText
    }

    private fun showEmptyView(shouldShowEmptyView: Boolean) {
        if (shouldShowEmptyView) {
            binding.trendingAllWebinarsRecy.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_webinars)
        } else {
            binding.trendingAllWebinarsRecy.visibility = View.VISIBLE
            binding.noDataAvailable.root.visibility = View.GONE
        }
    }

    private fun addLanguages(allDemoLectures: List<AllWebinarsApi>): List<AllWebinarsApi>{
        (application as MyApplication).getLanguages(object : MyApplication.FetchLanguagesListener{
            override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
                for(demoLecture in allDemoLectures){
                    demoLecture.languages?.let { languagesNotNull ->
                        demoLecture.languagesTextToShow = getSelectedLanguagesString(application,languagesNotNull.fromBase64(),)
                    }
                }
            }

        })
        for(demoLecture in allDemoLectures){
            demoLecture.languages?.let { languagesNotNull ->
                demoLecture.languagesTextToShow = getSelectedLanguagesString(application,languagesNotNull.fromBase64(),)
            }
        }
        return allDemoLectures
    }



    override fun onTrendingWebinarCardClick(webinarDetails: AllWebinarsApi) {
        webinarDetails.id.let { id ->
            WebinarDetailsActivity.launchActivity(this, id)
        }
    }

    override fun onShareClick(webinarDetails: AllWebinarsApi) {
        shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + webinarDetails.code, this)
    }
}