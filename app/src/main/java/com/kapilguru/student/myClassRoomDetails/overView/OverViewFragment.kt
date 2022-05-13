package com.kapilguru.student.myClassRoomDetails.overView

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kapilguru.student.*
import com.kapilguru.student.announcement.newMessage.data.NewMessageData
import com.kapilguru.student.customUI.CommonDialogFragment
import com.kapilguru.student.databinding.ActivityOverViewBinding
import com.kapilguru.student.myClassRoomDetails.model.BatchDetailsData
import com.kapilguru.student.myClassRoomDetails.model.RaiseComplaintRequest
import com.kapilguru.student.myClassRoomDetails.model.RefundRequest
import com.kapilguru.student.myClassRoomDetails.viewModel.MyClassDetailsViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences

class OverViewFragment : Fragment(), CommonDialogFragment.CommonDialogListener {

    lateinit var binding: ActivityOverViewBinding
    private val viewModels: MyClassDetailsViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var complaintRefundDialog: CommonDialogFragment
    private var userId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityOverViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        userId = StorePreferences(requireContext()).studentId.toString()
        viewModelObserver()
        viewModels.getBatchDetails()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnRaiseComplaint.setOnClickListener {
            viewModels.isComplaint.value = true
            complaintRefundDialog = CommonDialogFragment.newInstance("Raise Complaint", "Lets us know the reason", false)
            complaintRefundDialog.setDialogueListener(listener = this)
            complaintRefundDialog.show(parentFragmentManager, "")
        }

        binding.btnRequestRefund.setOnClickListener {
            viewModels.isComplaint.value = false
            complaintRefundDialog = CommonDialogFragment.newInstance("Refund Request", "Lets us know the reason", true)
            complaintRefundDialog.setDialogueListener(listener = this)
            complaintRefundDialog.show(parentFragmentManager, "")
        }
    }

    private fun setRequestRefundVisibility(batchData: BatchDetailsData?) {
        batchData?.let { batchDataNotNull ->
            if (shouldShowRequestRefund(batchDataNotNull)) {
                showOrHideRequestRefund(true,)
                setRefundButtonName(batchDataNotNull)
                hideRaiseComplaint()
            } else {
                showOrHideRequestRefund(false)
            }
        }
    }

    private fun shouldShowRequestRefund(batchData: BatchDetailsData): Boolean {
        return if (batchData.isAddedByTrainer == 1) {
            false
        } else {
            batchData.conductedCount <= 2
        }
    }

    private fun showOrHideRequestRefund(shouldShow: Boolean) {
        if (shouldShow) {
            binding.btnRequestRefund.visibility = View.VISIBLE
            binding.tvRefundRequestNote.visibility = View.VISIBLE
        } else {
            binding.btnRequestRefund.visibility = View.GONE
            binding.tvRefundRequestNote.visibility = View.GONE
        }
    }

    private fun hideRaiseComplaint() {
        binding.btnRaiseComplaint.visibility = View.GONE
        binding.tvRefundRequestNote.visibility = View.GONE
    }

    private fun setRefundButtonName(batchData: BatchDetailsData) {
        binding.btnRequestRefund.isEnabled = false
            if (batchData.refundFeeReqId != 0 && batchData.isRefunded == 0) {
                binding.btnRequestRefund.text = "Refunding"
            } else if (batchData.isRefunded != 0) {
                binding.btnRequestRefund.text = "Refunded"
            }

    }

    private fun viewModelObserver() {
        viewModels.batchApiResponse.observe(this.viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val batchData = it.data?.batchDetailsData?.get(0)
                    binding.model = batchData
                    viewModels.batchCode = it.data?.batchDetailsData?.get(0)?.batchCode ?: ""
                    setRequestRefundVisibility(batchData)
                    progressDialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(it.code){
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
                }

            }
        })

        viewModels.raiseComplaintResponse.observe(this.viewLifecycleOwner, {
            when (it?.status) {
                Status.SUCCESS -> {
                    if (isDialogueShowing()) {
                        complaintRefundDialog.clearText()
                        complaintRefundDialog.shouldShowLoading(false)
                        complaintRefundDialog.dismiss()
                        showAppToastCenter(requireContext(), it.data?.message!!)
                    }
                }
                Status.LOADING -> {
                    complaintRefundDialog.shouldShowLoading(true)
                }
                Status.ERROR -> {
                    complaintRefundDialog.dismiss()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
                }
            }
        })

        viewModels.refundResponse.observe(this.viewLifecycleOwner, {
            when (it?.status) {
                Status.SUCCESS -> {
                    if (isDialogueShowing()) {
                        complaintRefundDialog.clearText()
                        complaintRefundDialog.shouldShowLoading(false)
                        complaintRefundDialog.dismiss()
                        showAppToastCenter(requireContext(), it.data?.message!!)
                    }
                }
                Status.LOADING -> {
                    complaintRefundDialog.shouldShowLoading(true)
                }
                Status.ERROR -> {
                    complaintRefundDialog.dismiss()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
                }
            }
        })

    }

    companion object {

        fun launchActivity(batchData: NewMessageData?, activity: Activity) {
            val intent = Intent(activity, OverViewFragment::class.java)
            val bundle = Bundle()
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    private fun returnRefundApi(reason: String) {
        val request = RefundRequest(userId, binding.model?.trainerId?.toInt()!!, binding.model?.courseId!!, binding.model?.batchId!!, reason)
        viewModels.refundRequest(request)
    }

    private fun raiseComplaintApi(reason: String) {
        val request = RaiseComplaintRequest(userId, binding.model?.trainerId?.toInt()!!, reason)
        viewModels.raiseComplaint(request)
    }

    private fun isDialogueShowing(): Boolean {
        return complaintRefundDialog.dialog != null && complaintRefundDialog.dialog?.isShowing!!
    }

    override fun onReasonSubmitted(reason: String) {
        if (reason.isNotEmpty()) {
            when (viewModels.isComplaint.value) {
                true -> raiseComplaintApi(reason)
                else -> returnRefundApi(reason)
            }
        } else {
            showAppToast(requireContext(), "Please provide the reason")
        }
    }

}