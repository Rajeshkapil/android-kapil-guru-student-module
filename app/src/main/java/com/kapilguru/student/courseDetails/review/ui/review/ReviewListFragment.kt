package com.kapilguru.student.courseDetails.review.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.CourseDetailsViewModel
import com.kapilguru.student.courseDetails.review.model.StudentReviewedData
import com.kapilguru.student.network.Status
import kotlinx.android.synthetic.main.fragment_review_list_item.*

/**
 * A fragment representing a list of Items.
 */
class ReviewListFragment : Fragment() {

    private val viewModel: CourseDetailsViewModel by activityViewModels()
    private lateinit var adapter: MyReviewListRecyclerViewAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_list_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = CustomProgressDialog(requireContext())
        adapter = MyReviewListRecyclerViewAdapter()
        list.adapter = adapter
        viewModel.courseId.value?.let { viewModel.getStudentsReviewsList(it) }

        observeViewModel()



    }

    private fun observeViewModel() {
        viewModel.studentListReviewResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
                    response?.let { it ->
                        if(response.isNotEmpty())
                        adapter.setData(it as ArrayList<StudentReviewedData>)
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }


}