package com.kapilguru.student.webinar.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentLiveClassBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.videoCall.VideoCallInterfaceImplementation
import com.kapilguru.student.webinar.model.OnGoingWebinar
import com.kapilguru.student.webinar.viewModel.WebinarViewModel
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity
import com.kapilguru.student.webinar.webinarDetails.model.WebinarDetailsResData

class LiveWebinarFragment : Fragment(), LiveWebinarAdapter.LiveWebinarClickListener {
    private val TAG = "LiveClassFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: WebinarViewModel by activityViewModels()
    lateinit var adapter: LiveWebinarAdapter
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
        adapter = LiveWebinarAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
   not set to mutable live data.*/
    private fun setEmptyViewText(){
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_live_webinars)
    }

    private fun observeViewModelData() {
        viewModel.liveUpComingClasses.observe(viewLifecycleOwner, { liveUpcoming ->
            setAdapterData(liveUpcoming)
        })
        observeWebinarDetailsApiRes()
    }

    private fun setAdapterData(liveWebinarList: ArrayList<OnGoingWebinar>) {
        val onlyLiveWebinars = liveWebinarList.filter { liveWebinar ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveWebinar.startDate!!)
            liveClassMinutesLimit(differenceInMilliSeconds)
        }
        onlyLiveWebinars?.let { liveWebinar ->
            viewModel.liveWebinars.value = liveWebinar
            adapter.setData(viewModel.liveWebinars.value as ArrayList<OnGoingWebinar>)
        }
    }

    private fun observeWebinarDetailsApiRes() {
        viewModel.webinarDetailAPiRes.observe(viewLifecycleOwner, Observer { webinarDetailsApiRes ->
            when (webinarDetailsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    webinarDetailsApiRes.data?.let { webinarDetailsResData ->
                        webinarDetailsResData.webinarDetailsDataList?.get(0)?.let { webinarData ->
                            launchVideoMeet(webinarData)
                            progressDialog.dismissLoadingDialog()
                        }
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(webinarDetailsApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
                }
            }
        })
    }

    private fun launchVideoMeet(webinarDetails: WebinarDetailsResData) {
        val roomName = webinarDetails.roomname?: "room Name"
        val participantName = StorePreferences(requireContext()).userName
        VideoCallInterfaceImplementation.launchVideoCall(requireContext(),roomName,participantName)
    }

    override fun shareClicked(liveWebinar: OnGoingWebinar) {
        shareIntent(BuildConfig.SHARE_URL + WEBINAR_DETAILS + liveWebinar.webinarCode, requireContext())
    }

    override fun viewMoreClicked(liveWebinar: OnGoingWebinar) {
        liveWebinar.webinarId?.let { id->
            WebinarDetailsActivity.launchActivity(requireActivity(), id)
        }
    }

    override fun onGoLiveClicked(liveWebinar: OnGoingWebinar) {
        viewModel.getWebinarDetails(liveWebinar.webinarId.toString())
    }
}