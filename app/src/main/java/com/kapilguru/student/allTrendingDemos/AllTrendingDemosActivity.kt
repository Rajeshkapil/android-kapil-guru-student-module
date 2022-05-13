package com.kapilguru.student.allTrendingDemos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.allTrendingDemos.viewModel.AllTrendingDemosViewModel
import com.kapilguru.student.allTrendingDemos.viewModel.AllTrendingDemosViewModelFactory
import com.kapilguru.student.databinding.ActivityAllTrendingDemosBinding
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.homeActivity.dashboard.tabFragments.TrendingDemoAdapter
import com.kapilguru.student.homeActivity.models.AllDemosApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.model.LanguageData


class AllTrendingDemosActivity : BaseActivity(), TrendingDemoAdapter.TrendingDemosCardClick {
    lateinit var binding: ActivityAllTrendingDemosBinding
    lateinit var viewModel: AllTrendingDemosViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var adapter: TrendingDemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.demo_lecture))
        observeViewModelData()
        fetchDemoLectures()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_trending_demos)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, AllTrendingDemosViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllTrendingDemosViewModel::class.java)
        adapter = TrendingDemoAdapter(this, true)
        binding.trendingAllWebinarsRecy.adapter = adapter
    }

    private fun observeViewModelData() {
        viewModel.alDemosData.observe(this, Observer { response ->
            when (response.status) {

                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data.let { allDemoLectures ->
                        checkAndSetAdapterData(allDemoLectures)
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    if(response.code == NETWORK_CONNECTIVITY_EROR) {
                        showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                    }
                }
            }
        })
    }

    private fun fetchDemoLectures() {
        viewModel.fetchAllDemos()
    }

    private fun checkAndSetAdapterData(allDemoLectures: List<AllDemosApi>?) {
        allDemoLectures?.let { allDemoLecturesNotNull ->
            if (allDemoLecturesNotNull.isEmpty()) {
                showEmptyView(true)
            } else {
                showEmptyView(false)
                setAdapterData1(allDemoLecturesNotNull)
            }
        } ?: kotlin.run {
            showEmptyView(true)
        }
    }

    private fun setAdapterData1(allDemoLectures: List<AllDemosApi>){
        val allDemoLecturesWithLanguagesText = addLanguages(allDemoLectures)
        adapter.upComingScheduleApiList = allDemoLecturesWithLanguagesText as ArrayList<AllDemosApi>
    }

    private fun showEmptyView(shouldShow: Boolean) {
        if (shouldShow) {
            binding.trendingAllWebinarsRecy.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_demo_lectures)
        } else {
            binding.trendingAllWebinarsRecy.visibility = View.VISIBLE
            binding.noDataAvailable.root.visibility = View.GONE
        }
    }

    private fun addLanguages(allDemoLectures: List<AllDemosApi>): List<AllDemosApi>{
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

    override fun onTrendingDemosCardClick(upComingScheduleApi: AllDemosApi) {
        upComingScheduleApi.id.let { id ->
            DemoLectureDetailsActivity.launchActivity(this, id)
        }
    }

    override fun onShareClick(demosDetails: AllDemosApi) {
        shareIntent(BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + demosDetails.code, this)
    }
}