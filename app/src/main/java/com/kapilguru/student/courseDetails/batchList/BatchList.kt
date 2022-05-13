package com.kapilguru.student.courseDetails.batchList

import android.os.Bundle
import android.text.TextUtils
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.PARAM_BATCHES_LIST
import com.kapilguru.student.PAYMENT_PRODUCT_TYPE_COURSE
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.model.BatchesItem
import com.kapilguru.student.payment.CoursePaymentPreviewActivity
import com.kapilguru.student.payment.model.BatchDays
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.courseDetails.CourseDetailsModelFactory
import com.kapilguru.student.courseDetails.CourseDetailsViewModel
import com.kapilguru.student.courseDetails.model.BatchRequest
import com.kapilguru.student.customUI.CommonDialogFragment
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.android.synthetic.main.activity_batch_list.*
import kotlinx.android.synthetic.main.activity_batch_list.custom_action_bar

class BatchList : BaseActivity(), BatchAdapter.OnItemClick, CommonDialogFragment.CommonDialogListener{

    lateinit var batchAdapter: BatchAdapter
    private lateinit var requestBatchDialog: CommonDialogFragment
    var isCourseEnrolled: Boolean? = false
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: CourseDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_list)
        viewModel = ViewModelProvider(this, CourseDetailsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(CourseDetailsViewModel::class.java)
        viewModel.courseId.value = intent.getStringExtra(PARAM_COURSE_ID)
        progressDialog = CustomProgressDialog(this)
        setActionbarBackListener(this,custom_action_bar,resources.getString(R.string.batch_details))
        setUpRecycler()
        setData()
        viewModelObservers()
        btnAddbatch.setOnClickListener { showRequestBatchDialog() }
    }

    private fun viewModelObservers() {
        viewModel.requestBatchResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, it.data!!.message)
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, it.data!!.message)
                }
            }
        })
    }

    private fun setUpRecycler() {
        batchAdapter = BatchAdapter(this)
        recycler.adapter = batchAdapter
    }

    private fun showRequestBatchDialog() {
        requestBatchDialog = CommonDialogFragment.newInstance("Request Batch", "Lets us know the reason", false)
        requestBatchDialog.setDialogueListener(this)
        requestBatchDialog.show(supportFragmentManager, "")
    }

    private fun setData() {
        val data: ArrayList<BatchesItem>? = intent?.getParcelableArrayListExtra(PARAM_BATCHES_LIST)
         isCourseEnrolled = intent?.getBooleanExtra(PARAM_IS_ENROLLED,false)
        if (data != null && data.size > 0) {
            batchAdapter.item = data
        } else {
            noDataAvailable.visibility = View.VISIBLE
        }
    }

    override fun onCardClick(batchItem: BatchesItem) {
        if(isCourseEnrolled!!){
            Toast.makeText(this, resources.getString(R.string.already_purchased_course), Toast.LENGTH_LONG).show()
        } else {
            val initiateTransactionRequest = InitiateTransactionRequest()
            initiateTransactionRequest.amount = batchItem.discountedPrice
            initiateTransactionRequest.productType = PAYMENT_PRODUCT_TYPE_COURSE
            initiateTransactionRequest.productId = batchItem.id
            initiateTransactionRequest.productCode = batchItem.code
            initiateTransactionRequest.courseId = batchItem.courseId
            val startDate = batchItem.startDate ?: ""
            val endDate = batchItem.endDate ?: ""
            val trainerId = batchItem.trainerId ?: 0
            val courseId = batchItem.courseId ?: 0
            val batchDays: BatchDays = BatchDays().apply {
                this.mon = batchItem.dayMon
                this.tue = batchItem.dayTue
                this.wed = batchItem.dayWed
                this.thu = batchItem.dayThu
                this.fri = batchItem.dayFri
                this.sat = batchItem.daySat
                this.sun = batchItem.daySun
            }
            CoursePaymentPreviewActivity.launchActivity(initiateTransactionRequest, startDate, endDate, trainerId, courseId, batchDays, this)
        }
    }

    override fun onReasonSubmitted(reason: String) {
        if (!TextUtils.isEmpty(reason)){
            val studentId = StorePreferences(this).studentId
            requestBatchDialog.dismiss()
            viewModel.batchRequest(BatchRequest( viewModel.courseId.value!!.toInt(),studentId,reason))
        }else{
            showAppToast(this,"Please provide the reason")
        }
    }


}