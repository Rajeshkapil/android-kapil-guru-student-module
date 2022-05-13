package com.kapilguru.student.myClassroom.totalActiveBatches

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentActiveCoursesBinding
import com.kapilguru.student.datesDifferenceInMilli
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData
import com.kapilguru.student.myClassroom.viewModel.MyClassroomViewModel
import com.kapilguru.student.searchCourse.SearchCourseActivity
import java.util.*
import kotlin.collections.ArrayList

class ActiveCoursesFragment : Fragment(), ActiveCoursesAdapter.CourseClickListerner {
    private val TAG = "ActiveBatchesFragment"
    lateinit var binding: FragmentActiveCoursesBinding
    val viewModel: MyClassroomViewModel by viewModels(ownerProducer = { requireParentFragment() })
    lateinit var adapter: ActiveCoursesAdapter
    lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveCoursesBinding.inflate(inflater, container, false)
        progressDialog = CustomProgressDialog(requireContext())
        setAdapter()
        return binding.root
    }

    private fun setAdapter() {
        adapter = ActiveCoursesAdapter(this)
        binding.rvBatch.adapter = adapter
        setViewVisibleHide(viewModel.activeBatchesList1.isEmpty())
        if (viewModel.activeBatchesList1.isNotEmpty()) {
            categoriseBatchesAndSetData(viewModel.activeBatchesList1)
        }
    }

    private fun categoriseBatchesAndSetData(batchList: ArrayList<ActiveBatchData>) {
        val listToShow: ArrayList<ActiveBatchData> // show either active or expired list
        val shouldShowLiveCourse = arguments?.getBoolean(KEY_SHOULD_SHOW_LIVE_COURSES) ?: true
        listToShow = batchList.filter { it ->
            val differenceInMilliSeconds = datesDifferenceInMilli(it.endDate!!)
            if (shouldShowLiveCourse) {
                differenceInMilliSeconds > 0
            } else {
                differenceInMilliSeconds < 0
            }
        } as ArrayList<ActiveBatchData>
        val courseBatchList = LinkedHashMap<Int, ArrayList<ActiveBatchData>>()
        for (batch in listToShow) {
            if (courseBatchList.containsKey(batch.courseId)) {
                val list = courseBatchList.get(batch.courseId)
                list?.add(batch)
                courseBatchList.put(batch.courseId, list!!)
            } else {
                val list = ArrayList<ActiveBatchData>()
                list.add(batch)
                courseBatchList.put(batch.courseId, list)
            }
        }
        adapter.setData(courseBatchList)
    }

    private fun setViewVisibleHide(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvBatch.visibility = View.GONE
            binding.noDataAvailable.tvNoData.visibility = View.VISIBLE
            val shouldShowLiveCourse = arguments?.getBoolean(KEY_SHOULD_SHOW_LIVE_COURSES) ?: true
            if(shouldShowLiveCourse){
                binding.noDataAvailable.tvNoData.text = getString(R.string.no_active_classes)
                binding.noDataAvailable.btnEnrollNow.visibility = View.VISIBLE
                binding.noDataAvailable.btnEnrollNow.text = getString(R.string.enroll_now_pre_course_text)
                setEnrollNowClickListener()
            }else{
                binding.noDataAvailable.tvNoData.text = getString(R.string.no_completed_classes)
            }
        } else {
            binding.rvBatch.visibility = View.VISIBLE
            binding.noDataAvailable.tvNoData.visibility = View.GONE
        }
    }

    private fun setEnrollNowClickListener(){
        binding.noDataAvailable.btnEnrollNow.setOnClickListener {
            SearchCourseActivity.launchActivity(requireActivity())
        }
    }

    override fun onCourseClicked(batchList: ArrayList<ActiveBatchData>) {
        val intent = Intent(requireContext(), ActiveBatchesActivity::class.java)
        intent.putParcelableArrayListExtra(ActiveBatchesActivity.batch_list, batchList)
        intent.putExtra(ActiveBatchesActivity.course_name, batchList[0].courseTitle)
        startActivity(intent)
    }

    companion object {
        const val KEY_SHOULD_SHOW_LIVE_COURSES = "should_show_live_courses"

        fun newInstance(shouldShowLiveCourse: Boolean): ActiveCoursesFragment {
            val args = Bundle()
            args.putBoolean(KEY_SHOULD_SHOW_LIVE_COURSES, shouldShowLiveCourse)
            val fragment = ActiveCoursesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}