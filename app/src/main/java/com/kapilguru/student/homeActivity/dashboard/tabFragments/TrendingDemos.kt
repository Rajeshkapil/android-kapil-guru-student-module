package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kapilguru.student.*
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModel
import com.kapilguru.student.homeActivity.dashboard.DashBoardViewModelFactory
import com.kapilguru.student.homeActivity.models.AllDemosApi
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import kotlinx.android.synthetic.main.fragment_trending_webinars.view.*

class TrendingDemos : Fragment(), TrendingDemoAdapter.TrendingDemosCardClick {

    lateinit var viewModel: DashBoardViewModel
    lateinit var trendingWebinarAdapter: TrendingDemoAdapter
    lateinit var dialog: CustomProgressDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trending_webinars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(this.requireContext())
        trendingWebinarAdapter = TrendingDemoAdapter(this, false)
        viewModel = ViewModelProvider(this.requireParentFragment(), DashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(DashBoardViewModel::class.java)
        setUPRecycler(view)
        viewModelObserver()
    }

    private fun setUPRecycler(view: View) {
        view.trendingWebinarsRecy.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view.trendingWebinarsRecy.adapter = trendingWebinarAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrendingDemos()
    }

    fun viewModelObserver() {
        viewModel.fetchAllDemos()
        viewModel.alDemosData.observe(requireActivity(), Observer { response ->
            when (response.status) {

                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { upComingSchedule ->
                        trendingWebinarAdapter.upComingScheduleApiList = upComingSchedule
                        viewModel.trendingDemosList = upComingSchedule as ArrayList<AllDemosApi>
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })

    }

    override fun onTrendingDemosCardClick(upComingScheduleApi: AllDemosApi) {
        upComingScheduleApi.id.let { id ->
            DemoLectureDetailsActivity.launchActivity(requireActivity(), id)
        }
    }

    override fun onShareClick(demosDetails: AllDemosApi) {
        shareIntent(BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + demosDetails.code, requireContext())
    }
}