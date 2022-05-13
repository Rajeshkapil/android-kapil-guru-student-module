package com.kapilguru.student.myClassroom.upComingClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentUpcomingClassBinding
import com.kapilguru.student.datesDifferenceInMilli
import com.kapilguru.student.liveClassMinutesLimit
import com.kapilguru.student.myClassRoomDetails.MyClassDetailsActivity
import com.kapilguru.student.myClassroom.liveClasses.LiveUpComingClassAdapter
import com.kapilguru.student.myClassroom.liveClasses.model.LiveUpComingClassData
import com.kapilguru.student.myClassroom.viewModel.MyClassroomViewModel

class UpcomingClassFragment : Fragment(), LiveUpComingClassAdapter.LiveUpComingClassClickListener {
    private val TAG = "UpcomingClassFragment"
    lateinit var binding: FragmentUpcomingClassBinding
    val viewModel: MyClassroomViewModel by viewModels(ownerProducer = { requireParentFragment() })
    lateinit var adapter: LiveUpComingClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpcomingClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setAdapterData()
    }

    private fun setAdapter() {
        adapter = LiveUpComingClassAdapter(this)
        binding.rvUpcoimgClasses.adapter = adapter
    }

    private fun setAdapterData() {
        val liveUpComingClassList = viewModel.liveUpComingClassDataList.value
        val upComingClassList = liveUpComingClassList?.filter { liveClassItem ->
            val differenceInMilliSeconds = datesDifferenceInMilli(liveClassItem.startTime!!)
            !liveClassMinutesLimit(differenceInMilliSeconds)
        }
        upComingClassList?.let { liveClassItem ->
            setViewVisibleHide(liveClassItem.isEmpty())
            if (liveClassItem.isNotEmpty()) {
                viewModel.upComingClasses.value = liveClassItem
                adapter.setData(viewModel.upComingClasses.value as ArrayList<LiveUpComingClassData>)
            }
        }?: kotlin.run {
            setViewVisibleHide(true)
        }
    }

    private fun setViewVisibleHide(isEmpty: Boolean) {
        Log.d(TAG, "setViewVisibleHide: isEmpty : "+isEmpty)
        if (isEmpty) {
            binding.rvUpcoimgClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.text = getString(R.string.no_upcoming_classes)
        } else {
            binding.rvUpcoimgClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }


    override fun onLiveUpComingClassClicked(liveUpComingClass: LiveUpComingClassData) {

    }

    override fun onOverViewClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 0)
    }

    override fun onRecordingsClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 1)
    }

    override fun onStudyMaterialClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 2)
    }

    override fun onExamClicked(liveUpComingClass: LiveUpComingClassData) {
        MyClassDetailsActivity.launchActivity(liveUpComingClass.batchId.toString(), requireActivity(), 3)
    }
}