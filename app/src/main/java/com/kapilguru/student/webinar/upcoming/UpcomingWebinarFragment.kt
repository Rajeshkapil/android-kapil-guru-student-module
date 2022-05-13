package com.kapilguru.student.webinar.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentLiveClassBinding
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.webinar.model.OnGoingWebinar
import com.kapilguru.student.webinar.viewModel.WebinarViewModel
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity

class UpcomingWebinarFragment : Fragment(), UpComingWebinarAdapter.UpComingWebinarClickListener {
    private val TAG = "UpcomingWebinarFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: WebinarViewModel by activityViewModels()
    lateinit var adapter: UpComingWebinarAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLiveClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        observeViewModelData()
        setEmptyViewText()
    }

    private fun setAdapter() {
        adapter = UpComingWebinarAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
   not set to mutable live data.*/
    private fun setEmptyViewText(){
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_upcoming_webinars)
    }

    private fun observeViewModelData() {
        viewModel.liveUpComingClasses.observe(viewLifecycleOwner, { upComing ->
            setAdapterData(upComing)
        })
    }

    private fun setAdapterData(liveUpComingWebinarList : ArrayList<OnGoingWebinar>) {
        val upComingWebinars = liveUpComingWebinarList?.filter { liveOnGoingWebinar ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveOnGoingWebinar.startDate!!)
            !liveClassMinutesLimit(differenceInMilliSeconds)
        }
        upComingWebinars?.let { upComingWebinar ->
            viewModel.onGoingWebinars.value = upComingWebinar
            adapter.setData(viewModel.onGoingWebinars.value as ArrayList<OnGoingWebinar>)
        }
    }

    override fun shareClicked(upComingClass: OnGoingWebinar) {
        shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + upComingClass.webinarCode, requireContext())
    }

    override fun viewMoreClicked(upComingClass: OnGoingWebinar) {
        upComingClass.webinarId?.let {
            WebinarDetailsActivity.launchActivity(requireActivity(), it)
        }
    }
}