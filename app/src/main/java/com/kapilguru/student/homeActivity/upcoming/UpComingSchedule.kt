package com.kapilguru.student.homeActivity.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentUpComingSchedueleBinding
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.homeActivity.dashboard.TodayScheduleAdapter
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi
import com.kapilguru.student.myClassRoomDetails.MyClassDetailsActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity
import java.util.*

class UpComingSchedule : Fragment(), TodayScheduleAdapter.OnItemClick {
    private val TAG = "UpComingSchedule"
    lateinit var binding: FragmentUpComingSchedueleBinding
    lateinit var viewModel: UpComingViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var todayScheduleAdapter: TodayScheduleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_up_coming_scheduele, container, false)
        viewModel = ViewModelProvider(this, UpcomingViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(UpComingViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireActivity())
        todayScheduleAdapter = TodayScheduleAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUpcomingSchedule()
        binding.horizontalRecycler.adapter = todayScheduleAdapter
        shouldShowActionBarSearch(true)
        observeViewModel()
//        ((activity) as HomeActivity).fetchLatestNotification()
    }

    private fun shouldShowActionBarSearch(shouldShowActionSearchBar: Boolean){
        ((activity) as HomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }

    private fun observeViewModel() {
        viewModel.upcomingResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { upComingSchedule ->
                        if (upComingSchedule.isNotEmpty()) {
                            todayScheduleAdapter.upComingScheduleApiList = upComingSchedule
                        } else {
                            showEmptyView(true)
                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            showSingleButtonErrorDialog(requireContext(),getString(R.string.network_connection_error))
                        }
                    }
                }
            }
        })
    }

    override fun onCardClick(upComingScheduleApi: UpComingScheduleApi) {
        when (upComingScheduleApi.activityType?.toLowerCase()) {
            "course" -> {
                MyClassDetailsActivity.launchActivity(upComingScheduleApi.activityId.toString(), requireActivity(), 0)
            }
            "webinar" -> {
                upComingScheduleApi.activityId?.let { id ->
                    WebinarDetailsActivity.launchActivity(requireActivity(), id)
                }
            }
            "lecture" -> {
                upComingScheduleApi.activityId?.let { id ->
                    WebinarDetailsActivity.launchActivity(requireActivity(), id)
                }
            }
        }
    }

    private fun showEmptyView(shouldShow: Boolean) {
        if (shouldShow) {
            binding.horizontalRecycler.visibility = View.GONE
            binding.noDataAvailable.root.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_upcoming_schedules)
        } else {
            binding.horizontalRecycler.visibility = View.VISIBLE
            binding.noDataAvailable.root.visibility = View.GONE
        }
    }

}