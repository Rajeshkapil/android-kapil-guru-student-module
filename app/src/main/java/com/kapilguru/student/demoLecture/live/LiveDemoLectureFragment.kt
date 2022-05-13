package com.kapilguru.student.demoLecture.live

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentLiveClassBinding
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.demoLecture.demoLectureDetails.model.DemoLectureDetailsResData
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.demoLecture.viewModel.DemoLectureViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.videoCall.VideoCallInterfaceImplementation

class LiveDemoLectureFragment : Fragment(), LiveDemoLectureAdapter.LiveUpComingDemoLectureClickListener {
    private val TAG = "LiveDemoLectureFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: DemoLectureViewModel by activityViewModels()
    lateinit var adapter: LiveDemoLectureAdapter
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
        adapter = LiveDemoLectureAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
    not set to mutable live data.*/
    private fun setEmptyViewText(){
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_live_demo_lectures)
    }

    private fun observeViewModelData() {
        viewModel.liveOnGoingDemoLectures.observe(viewLifecycleOwner, { onGoingDemolectures ->
            setAdapterData(onGoingDemolectures)
        })
        observeDemoLectureDetailsApiRes()
    }

    private fun setAdapterData(liveDemoLectureList: ArrayList<OnGoingDemoLectures>) {
        val onlyLiveDemoLectureList = liveDemoLectureList.filter { liveDemoLecture ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveDemoLecture.startDate!!)
            liveClassMinutesLimit(differenceInMilliSeconds)
        }
        onlyLiveDemoLectureList.let { liveDemoLecture ->
            showOrHideEmptyView(liveDemoLecture.isEmpty())
            viewModel.liveDemoLectures.value = liveDemoLecture
            adapter.setData(viewModel.liveDemoLectures.value as ArrayList<OnGoingDemoLectures>)
        }
    }

    private fun showOrHideEmptyView(showEmptyView : Boolean){
        if(showEmptyView){
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_live_demo_lectures)
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
        }else{
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    private fun observeDemoLectureDetailsApiRes() {
        viewModel.demoLectureDetailsApiRes.observe(viewLifecycleOwner, Observer { demoLectureDetailsApiRes ->
            when (demoLectureDetailsApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    demoLectureDetailsApiRes.data?.let { demoLectureDetailsResData ->
                        try {
                            demoLectureDetailsResData.demoLectureDataList?.get(0)?.let { demoLectureData ->
                                launchVideoMeet(demoLectureData)
                            }
                        } catch (exception: IndexOutOfBoundsException) {

                        }
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun launchVideoMeet(demoLectureData: DemoLectureDetailsResData) {
        val roomName = demoLectureData.roomname
        val participantName = StorePreferences(requireContext()).userName
        VideoCallInterfaceImplementation.launchVideoCall(requireContext(),roomName,participantName)
    }

    override fun shareClicked(liveDemoLecture: OnGoingDemoLectures) {
        shareIntent(BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + liveDemoLecture.lectureCode, requireContext())
    }

    override fun viewMoreClicked(liveDemoLecture: OnGoingDemoLectures) {
        liveDemoLecture.lectureId?.let {id->
            DemoLectureDetailsActivity.launchActivity(requireActivity(),id)
        }
    }

    override fun onGoLiveClicked(liveDemoLecture: OnGoingDemoLectures) {
        liveDemoLecture.lectureId?.let {id->
            DemoLectureDetailsActivity.launchActivity(requireActivity(),id)
        }
    }
}