package com.kapilguru.student.allExamsList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityAllExamsListBinding
import com.kapilguru.student.exam.ViewExamResult
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest
import com.kapilguru.student.exam.questionPaper.QuestionPaperNewActivity
import com.kapilguru.student.myClassRoomDetails.exam.ExamListAdapter
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences

class AllExamsListActivity : BaseActivity(), ExamListAdapter.ExamItemClickListener {

    lateinit var binding: ActivityAllExamsListBinding
    lateinit var progressDialog: CustomProgressDialog
    lateinit var viewModel: AllExamsViewModel
    lateinit var adapter: ExamListAdapter
    var studentId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setCustomActionBar()
        observeViewModelData()
    }

    private fun observeViewModelData() {
        viewModel.getCompletionRequests(studentId.toString())

        viewModel.examsListResponseAPi.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    it.data?.questionPaperList?.let { questionPaperList ->
                        if (questionPaperList.isNotEmpty()) {
                            showNoDataVisibility(false)
                            setDataToAdapter(questionPaperList)
                        } else {
                            showNoDataVisibility(true)
                        }
                    }
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(it.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun showNoDataVisibility(boolean: Boolean) {
        binding.noDataAvailable.tvNoData.text = getString(R.string.no_exams)
        binding.noDataAvailable.tvNoData.visibility =  if(boolean) View.VISIBLE else View.GONE
    }

    private fun setDataToAdapter(questionPaperList: ArrayList<QuestionPaperListItemResData>) {
        adapter.setData(null,questionPaperList)
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_exams_list)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, AllExamsViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(AllExamsViewModel::class.java)
        binding.lifecycleOwner = this
        adapter = ExamListAdapter(this)
        binding.examListRecycler.adapter = adapter
        studentId = StorePreferences(this).studentId
    }

    private fun setCustomActionBar() {
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.all_exams))
    }


    override fun onStartExamClicked(questionPaper: QuestionPaperListItemResData) {
        QuestionPaperNewActivity.launchActivity(this, questionPaper)
    }



    override fun onViewResultClicked(questionPaper: QuestionPaperListItemResData) {
        launchViewResult(questionPaper)
    }

    private fun launchViewResult(questionPaper: QuestionPaperListItemResData) {
        val examReportRequest = StudentReportRequest(
            batchId = questionPaper.batchId,
            examId = questionPaper.examId,
            questionPaperId = questionPaper.questionPaperId,
            studentId = questionPaper.studentId,
            courseId = questionPaper.courseId,
            trainerId = questionPaper.trainerId
        )
        val questionRequest = QuestionsRequest(
            batchId = questionPaper.batchId,
            examId = questionPaper.examId,
            questionPaperId = questionPaper.questionPaperId,
            studentId = questionPaper.studentId,
        )
        startActivity(
            Intent(this, ViewExamResult::class.java)
                .putExtra(PARAM_REPORTS_REQUEST, examReportRequest)
                .putExtra(PARAM_QUESTIONS_REQUEST, questionRequest)
        )
    }

}