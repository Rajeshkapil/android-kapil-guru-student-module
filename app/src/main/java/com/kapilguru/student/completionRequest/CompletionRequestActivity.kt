package com.kapilguru.student.completionRequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.completionRequest.model.CompletionRequestResData
import com.kapilguru.student.completionRequest.model.UpdateCompletionRequest
import com.kapilguru.student.completionRequest.viewModel.CompletionRequestViewModel
import com.kapilguru.student.completionRequest.viewModel.CompletionRequestViewModelFactory
import com.kapilguru.student.customUI.CommonDialogFragment
import com.kapilguru.student.databinding.ActivityCompletionRequestBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences

class CompletionRequestActivity : AppCompatActivity(), CompletionRequestAdapter.CompletionRequestListener, CommonDialogFragment.CommonDialogListener {
    private val TAG = "CompletionRequestActivity"
    lateinit var binding: ActivityCompletionRequestBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: CompletionRequestViewModel
    lateinit var adapter: CompletionRequestAdapter
    private lateinit var rejectReasonDialog: CommonDialogFragment
    var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completion_request)
        initLateInitVariables()
        setActivityName()
        setClickListeners()
        observeViewModelData()
        fetchCompletionRequestList()
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = resources.getString(R.string.completion_requests)
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.actvTermsAndCond.setOnClickListener {
            changeVisibilityOfTermsAndConditions()
        }
    }

    private fun changeVisibilityOfTermsAndConditions() {
        if(binding.cvTermsAndConditions.visibility == View.VISIBLE){
            binding.cvTermsAndConditions.visibility = View.GONE
        }else{
            binding.cvTermsAndConditions.visibility = View.VISIBLE
        }
    }


    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_completion_request)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, CompletionRequestViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(CompletionRequestViewModel::class.java)
        binding.lifecycleOwner = this
        val studentName = StorePreferences(this).userName
        adapter = CompletionRequestAdapter(studentName, this)
        binding.rvCompletionRequest.adapter = adapter
        studentId = StorePreferences(this).studentId.toString()
    }

    private fun observeViewModelData() {
        observeCompletionRequestApiResponse()
        observeUpdateCompletionReqApiResponse()
    }

    private fun observeCompletionRequestApiResponse() {
        viewModel.completionRequestApiRes.observe(this, Observer { completionReqResApi ->
            when (completionReqResApi.status) {

                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    completionReqResApi.data?.let { completionRequestResponse ->
                        checkAndSetAdapterData(completionRequestResponse.completionReqList)
                        progressDialog.dismissLoadingDialog()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(completionReqResApi.code)  {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(this)
                        }
                    }
                }
            }
        })
    }

    private fun checkAndSetAdapterData(completionReqList : ArrayList<CompletionRequestResData>?){
        completionReqList?.let { completionReqListNotNull ->
            if(completionReqListNotNull.isEmpty()){
                showOrHideCompletionReqList(false)
            }
            else{
                showOrHideCompletionReqList(true)
                adapter.setData(completionReqList)
            }
        }?: kotlin.run {
            showOrHideCompletionReqList(false)
        }
    }

    private fun showOrHideCompletionReqList(shouldShow : Boolean){
        if(shouldShow){
            binding.tvEmptyCompletionRequest.visibility = View.GONE
            binding.rvCompletionRequest.visibility = View.VISIBLE

        }else{
            binding.tvEmptyCompletionRequest.visibility = View.VISIBLE
            binding.rvCompletionRequest.visibility = View.GONE
        }
    }

    private fun observeUpdateCompletionReqApiResponse() {
        viewModel.updateCompletionReqStatusApiRes.observe(this, Observer { updateCompletionReqApiRes ->
            when (updateCompletionReqApiRes.status) {

                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    updateCompletionReqApiRes.data?.let { updateCompletionReqResData ->
                        if (updateCompletionReqResData.status == 200) {
                            showAppToast(this, "Status Updated Successfully")
//                            fetchCompletionRequestList() // Inorder to update the ui
                        } else {

                        }
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(updateCompletionReqApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun fetchCompletionRequestList() {
        viewModel.getCompletionRequests(studentId)
    }

    override fun onRejectClicked(completionRequest: CompletionRequestResData) {
        viewModel.rejectCompletionRequest = completionRequest
        showRejectReasonDialog()
    }

    override fun onAcceptClicked(completionRequest: CompletionRequestResData) {
        val updateCompletionRequest = createRequest("",COMPLETION_STATUS_ACCEPTED, completionRequest)
        viewModel.updateCompletionReqStatus(studentId, updateCompletionRequest)
    }

    private fun showRejectReasonDialog() {
        rejectReasonDialog = CommonDialogFragment.newInstance("Reason For Rejection", "Lets us know the reason", false)
        rejectReasonDialog.setDialogueListener(this)
        rejectReasonDialog.show(supportFragmentManager, "")
    }

    private fun createRequest(rejectReason : String,status: String, completionRequest: CompletionRequestResData): UpdateCompletionRequest {
        val request = UpdateCompletionRequest()
        request.batchId = completionRequest.batchId!!
        request.bcrReqId = completionRequest.bcrReqId!!
        request.bcrReqRespReason = rejectReason
        request.bcrReqRespStatus = status
        return request
    }

    override fun onReasonSubmitted(reason: String) {
        if (reason.isNotEmpty()) {
            rejectReasonDialog.dismiss()
            val updateCompletionRequest = createRequest(reason,COMPLETION_STATUS_REJECTED, viewModel.rejectCompletionRequest)
            viewModel.updateCompletionReqStatus(studentId, updateCompletionRequest)
        } else {
            showAppToast(this, "Please provide the reason")
        }
    }

    companion object{
        fun launchActivity(activity : Activity){
            val intent = Intent(activity, CompletionRequestActivity::class.java)
            activity.startActivity(intent)
        }
    }
}