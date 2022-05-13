package com.kapilguru.student.myClassRoomDetails.exam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.*
import com.kapilguru.student.databinding.FragmentExamListBinding
import com.kapilguru.student.exam.ViewExamResult
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest
import com.kapilguru.student.exam.questionPaper.QuestionPaperActivity
import com.kapilguru.student.exam.questionPaper.QuestionPaperNewActivity
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListRequest
import com.kapilguru.student.myClassRoomDetails.viewModel.MyClassDetailsViewModel
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences

class ExamListFragment : Fragment(), ExamListAdapter.ExamItemClickListener {
    private val TAG = "ExamListFragment"
    private lateinit var binding: FragmentExamListBinding
    private val viewModel: MyClassDetailsViewModel by activityViewModels()
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var examListAdapter: ExamListAdapter
    private var userId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLateInitVariables()
        observeViewModelData()
        getQuestionPaperList()
    }

    private fun initializeLateInitVariables() {
        progressDialog = CustomProgressDialog(requireContext())
        examListAdapter = ExamListAdapter(this)
        binding.rvExam.adapter = examListAdapter
    }

    private fun observeViewModelData() {
        observeExamListApiRes()
    }

    private fun observeExamListApiRes() {
        viewModel.examListApiRes.observe(viewLifecycleOwner, Observer { questionPaperListApiRes ->
            when (questionPaperListApiRes.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    setAdapterData(questionPaperListApiRes.data?.questionPaperList)
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when (questionPaperListApiRes.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            networkConnectionErrorDialog(requireContext())
                        }
                    }
                }
            }
        })
    }

    private fun setAdapterData(examList: ArrayList<QuestionPaperListItemResData>?) {
        examList?.let { examListNotNull ->
            if (examListNotNull.isEmpty()) {
                showOrHideEmptyExamsView(true)
            } else {
                examListAdapter.setData(viewModel.batchCode,examList)
                showOrHideEmptyExamsView(false)
            }
        } ?: kotlin.run {
            showOrHideEmptyExamsView(true)
        }
    }

    private fun showOrHideEmptyExamsView(showExamsEmptyView: Boolean) {
        if (showExamsEmptyView) {
            binding.rvExam.visibility = View.GONE
            binding.actvEmptyView.visibility = View.VISIBLE
        } else {
            binding.rvExam.visibility = View.VISIBLE
            binding.actvEmptyView.visibility = View.GONE
        }
    }

    private fun getQuestionPaperList() {
        userId = StorePreferences(requireContext()).studentId.toString()
        val questionPaperListRequest = QuestionPaperListRequest(viewModel.batchId.value!!.toInt(), userId)
        viewModel.examListApiReq.value = questionPaperListRequest
        viewModel.getQuestionPaper()
    }

    override fun onStartExamClicked(questionPaper: QuestionPaperListItemResData) {
        QuestionPaperNewActivity.launchActivity(requireActivity(), questionPaper)
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
            Intent(requireContext(), ViewExamResult::class.java)
                .putExtra(PARAM_REPORTS_REQUEST, examReportRequest)
                .putExtra(PARAM_QUESTIONS_REQUEST, questionRequest)
        )
    }
}