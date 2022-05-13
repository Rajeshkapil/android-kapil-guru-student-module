package com.kapilguru.student.demoLecture.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentLiveClassBinding
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.demoLecture.viewModel.DemoLectureViewModel

class UpcomingDemoLectureFragment : Fragment(), UpComingDemoLectureAdapter.UpComingDemoLectureClickListener {
    private val TAG = "UpcomingDemoLectureFragment"
    lateinit var binding: FragmentLiveClassBinding
    val viewModel: DemoLectureViewModel by activityViewModels()
    lateinit var adapter: UpComingDemoLectureAdapter
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
        adapter = UpComingDemoLectureAdapter(this)
        binding.rvLiveClasses.adapter = adapter
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
    not set to mutable live data.*/
    private fun setEmptyViewText(){
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_upcoming_demo_lectures)
    }

    private fun observeViewModelData() {
        viewModel.liveOnGoingDemoLectures.observe(viewLifecycleOwner, { upComing ->
            Log.d(TAG, "observeViewModelData: ")

            if (upComing.isNotEmpty())
            {
                binding.rvLiveClasses.visibility = View.VISIBLE
                binding.noDataAvailable.tvNoData.visibility = View.GONE
                binding.noDataAvailable.btnEnrollNow.visibility = View.GONE

                setAdapterData(upComing)
            }else{
                binding.rvLiveClasses.visibility = View.GONE
                binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
                binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE

            }
        })
    }

    private fun setAdapterData(liveUpComingDemoLectures : ArrayList<OnGoingDemoLectures>) {
        if (liveUpComingDemoLectures.isEmpty()) {
            binding.rvLiveClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
        } else {
            val upComingDemoLectures = liveUpComingDemoLectures.filter { liveDemoLecture ->
                val differenceInMilliSeconds = datesDifferenceInMilli(liveDemoLecture.startDate!!)
                !liveClassMinutesLimit(differenceInMilliSeconds)
            }
            upComingDemoLectures.let { upComingDemoLecture ->
                showOrHideEmptyView(upComingDemoLecture.isEmpty())
                viewModel.onGoingDemoLectures.value = upComingDemoLecture
                adapter.setData(viewModel.onGoingDemoLectures.value as ArrayList<OnGoingDemoLectures>)
            }
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

    override fun shareClicked(upComingDemoLecture: OnGoingDemoLectures) {
        shareIntent(BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + upComingDemoLecture.lectureCode, requireContext())
    }

    override fun viewMoreClicked(upComingDemoLecture: OnGoingDemoLectures) {
        upComingDemoLecture.lectureId?.let {id->
            DemoLectureDetailsActivity.launchActivity(requireActivity(),id)
        }
    }
}