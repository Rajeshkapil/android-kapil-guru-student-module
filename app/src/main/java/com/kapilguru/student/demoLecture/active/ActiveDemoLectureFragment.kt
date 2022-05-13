package com.kapilguru.student.demoLecture.active

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kapilguru.student.*
import com.kapilguru.student.allTrendingDemos.AllTrendingDemosActivity
import com.kapilguru.student.databinding.FragmentActiveWebinarBinding
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures
import com.kapilguru.student.demoLecture.viewModel.DemoLectureViewModel

class ActiveDemoLectureFragment : Fragment(), ActiveDemoLectureAdapter.ActiveDemoLecturesClickListener {
    private val TAG = "ActiveDemoLectureFragment"
    lateinit var binding: FragmentActiveWebinarBinding
    val viewModel: DemoLectureViewModel by activityViewModels()
    lateinit var adapter: ActiveDemoLectureAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveWebinarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        observeViewModelData()
        setEmptyViewText()
    }

    /*Setting empty view text in OnCreate as the data is not being observed, might be because of observable data is empty or
     not set to mutable live data.*/
    private fun setEmptyViewText() {
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_active_demo_lectures)
        binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE
        binding.noDataAvailable.btnEnrollNow.text = getString(R.string.enroll_now_pre_free_lecture_text)
        binding.noDataAvailable.btnEnrollNow.setOnClickListener(View.OnClickListener { navigateToAllDemos() })
    }

    private fun setAdapter() {
        adapter = ActiveDemoLectureAdapter(this)
//        binding.rvActiveClasses.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvActiveClasses.adapter = adapter
    }

    private fun navigateToAllDemos() {
        startActivity(Intent(requireContext(), AllTrendingDemosActivity::class.java))
    }

    private fun observeViewModelData() {
        viewModel.activeDemoLectures.observe(viewLifecycleOwner, { activeDemoLecture ->
            //  adapter.setData(activeDemoLecture
            setAdapterData(activeDemoLecture)
        })
    }

    private fun setAdapterData(activeDemoLectures: ArrayList<ActiveDemoLectures>) {
        if (activeDemoLectures.isEmpty()) {
            binding.rvActiveClasses.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
        } else {
            binding.rvActiveClasses.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
            binding.noDataAvailable.btnEnrollNow.visibility = View.GONE
            adapter.setData(viewModel.activeDemoLectures.value as ArrayList<ActiveDemoLectures>)
        }
    }

    override fun onShareClicked(activeData: ActiveDemoLectures) {
        shareIntent(BuildConfig.SHARE_URL + DEMO_LECTURES_DETAILS + activeData.lectureCode, requireContext())
    }

    override fun onViewMoreClicked(activeData: ActiveDemoLectures) {
        activeData.lectureId?.let { id ->
            DemoLectureDetailsActivity.launchActivity(requireActivity(), id)
        }
    }
}

