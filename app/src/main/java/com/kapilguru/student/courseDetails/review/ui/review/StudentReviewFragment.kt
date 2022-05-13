package com.kapilguru.student.courseDetails.review.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.CourseDetailsViewModel
import com.kapilguru.student.courseDetails.review.model.WriteReviewRequest
import com.kapilguru.student.databinding.FragmentStudentReviewBinding
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.showAppToast


class StudentReviewFragment : Fragment() {
    val viewModel: CourseDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentStudentReviewBinding
    lateinit var dialog : CustomProgressDialog
    private var studentId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_student_review, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = CustomProgressDialog(requireContext())
        studentId = StorePreferences(requireContext()).studentId
        observeViewModel()
        binding.button3.setOnClickListener { viewModel.writeReviewUpdate(343,studentId.toString()) }
    }

    private fun observeViewModel() {
        viewModel.writeReviewResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    clearUiData()
                    showAppToast(requireContext(), "Success")
//                    viewModel.getStudentsReviewsList(studentId.toString())
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                    showAppToast(requireContext(), "Failed")
                }
            }
        })

        viewModel.studentListReviewResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val response = it.data?.data
//                    adapter.setData(response as ArrayList<StudentReviewedData>)
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun clearUiData() {
        viewModel.writeReviewRequest.postValue(WriteReviewRequest(0f,"","",0))
    }

    companion object {

        @JvmStatic
        fun newInstance() = StudentReviewFragment()
    }
}