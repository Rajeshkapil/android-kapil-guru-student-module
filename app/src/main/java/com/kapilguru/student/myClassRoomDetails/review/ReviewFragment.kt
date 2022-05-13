package com.kapilguru.student.myClassRoomDetails.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentReviewBinding
import com.kapilguru.student.myClassRoomDetails.model.ReviewRequest
import com.kapilguru.student.myClassRoomDetails.viewModel.MyClassDetailsViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.showAppToast
import kotlinx.android.synthetic.main.custom_key_value_view.view.*
import kotlinx.android.synthetic.main.image_text_horizontal.view.*


class ReviewFragment : Fragment() {
    lateinit var progressDialog: CustomProgressDialog
    private val viewModels: MyClassDetailsViewModel by activityViewModels()
    lateinit var binding: FragmentReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        binding.lifecycleOwner = this
        binding.review = viewModels.reviewRequest.value
        bindData()
        onClickLisetener()
        observerLiveData()
    }

    private fun observerLiveData() {
        viewModels.reviewResponse.observe(this.viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    clearData()
                    showAppToast(requireContext(), "Review Posted Successfully")
                    progressDialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }

            }
        })
    }

    private fun onClickLisetener() {
        binding.postBtn.setOnClickListener {
            binding.review?.let {
                when {
                    it.studentRating == 0f -> showAppToast(requireContext(), "Provide rating")
                    it.studentRatingText.isEmpty() -> showAppToast(requireContext(),"Provide feedback")
                    else -> viewModels.updateReview(it)
                }
            }
        }

        binding.resetBtn.setOnClickListener { clearData() }
    }

    private fun clearData() {
       binding.review?.let {
           it.studentRating = 0f
           it.studentRatingText = ""
           binding.review = it
       }
    }

    private fun bindData() {
        val pref = StorePreferences(requireContext())
        val data = viewModels.batchApiResponse.value?.data?.batchDetailsData?.get(0)
        data?.let {
            val reviewRequest = viewModels.reviewRequest.value
            reviewRequest?.studentId = pref.studentId.toString()
            reviewRequest?.batchId = data.batchId

            viewModels.reviewRequest.postValue(reviewRequest)
            binding.review = reviewRequest

            binding.keyValuePare.text_key.text = "Trainer"
            binding.keyValuePare.text_value.text = data.trainerName
            binding.studentNameTv.text = pref.userName
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = ReviewFragment()
    }
}