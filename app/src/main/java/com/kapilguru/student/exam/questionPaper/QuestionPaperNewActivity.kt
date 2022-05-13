package com.kapilguru.student.exam.questionPaper

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityQuestionPaperNewBinding
import com.kapilguru.student.exam.model.QuestionsAndOptions
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.Responses
import com.kapilguru.student.exam.model.SubmitQuestionRequest
import com.kapilguru.student.exam.questionPaper.viewModel.QuestionPaperViewModel
import com.kapilguru.student.exam.questionPaper.viewModel.QuestionPaperViewModelFactory
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import java.lang.reflect.Type

class QuestionPaperNewActivity : AppCompatActivity() {
    private val TAG = "QuestionPaperNewActivity"
    private lateinit var binding: ActivityQuestionPaperNewBinding
    private lateinit var viewModel: QuestionPaperViewModel
    private lateinit var progressDialog: CustomProgressDialog
    var timer: CountDownTimer? = null
    lateinit var adapter: QuestionsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        getIntentData()
        setActivityName()
        getQuestions()
        observeViewModelData()
        setTimerText()
        setClickListeners()
    }

    private fun initLateInitVariables() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_paper_new)
        viewModel = ViewModelProvider(this, QuestionPaperViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(QuestionPaperViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(this)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = QuestionsListAdapter(viewModel)
        val linearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvQuestion.layoutManager = linearLayoutManager
        binding.rvQuestion.adapter = adapter
    }

    private fun getIntentData() {
        val questionPaperInfo = intent.getParcelableExtra<QuestionPaperListItemResData>(QuestionPaperActivity.KEY_QUESTION_PAPER_INFO)
        viewModel.questionPaperInfo = questionPaperInfo ?: QuestionPaperListItemResData()
        binding.questionPaperInfo = questionPaperInfo
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.question_paper)
    }

    private fun getQuestions() {
        val questionPaper = viewModel.questionPaperInfo
        val questionRequest = QuestionsRequest(questionPaper.batchId, questionPaper.examId, questionPaper.questionPaperId, questionPaper.studentId)
        viewModel.getQuestionsRequest(questionRequest)
    }

    private fun observeViewModelData() {
        observeCurrentQuestionIndex()
        observeQuestionsApiResponse()
        observeQuestionSubmitResponse()
        observeAllQuestionsSubmitResponse()
    }

    private fun observeCurrentQuestionIndex() {
        viewModel.currentQuestionIndex.observe(this, Observer { questionPaperIndex ->
            setCurrentQuestion(questionPaperIndex)
            setActionButtonsVisibility(questionPaperIndex)
        })
    }

    private fun setCurrentQuestion(questionPaperIndex: Int) {
        viewModel.currentQuestionAndOption.value = viewModel.questionsAndOptions.value!![questionPaperIndex]
    }

    private fun setActionButtonsVisibility(questionPaperIndex: Int) {
        val lastQuestionIndex = viewModel.questionsAndOptions.value!!.size - 1
        if (questionPaperIndex == 0) {
            setBackBtnProperties(false, false)
            setNextBtnProperties(true, true)
            setSubmitBtnProperties(false, false)
        } else {
            if (questionPaperIndex == lastQuestionIndex) {
                setBackBtnProperties(true, true)
                setNextBtnProperties(false, false)
                setSubmitBtnProperties(true, true)
            } else {
                setBackBtnProperties(true, true)
                setNextBtnProperties(true, true)
                setSubmitBtnProperties(false, false)
            }
        }
    }

    private fun setBackBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnBack.visibility = View.VISIBLE
        } else {
            binding.btnBack.visibility = View.GONE
        }
        binding.btnBack.isEnabled = shouldBeEnabled
    }

    private fun setNextBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnNext.visibility = View.VISIBLE
        } else {
            binding.btnNext.visibility = View.GONE
        }
        binding.btnNext.isEnabled = shouldBeEnabled
    }

    private fun setSubmitBtnProperties(shouldShow: Boolean, shouldBeEnabled: Boolean) {
        if (shouldShow) {
            binding.btnSubmit.visibility = View.VISIBLE
        } else {
            binding.btnSubmit.visibility = View.GONE
        }
        binding.btnSubmit.isEnabled = shouldBeEnabled
    }

    private fun observeQuestionsApiResponse() {
        viewModel.questionsResponse.observe(this, Observer { questionsApiResponse ->
            when (questionsApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    val questions = questionsApiResponse.data?.questionsApi?.questions
                    val responses = questionsApiResponse.data?.questionsApi?.responses
                    var responsesList = ArrayList<Responses>()
                    val gson = Gson()
                    responses?.let { responsesNotNull ->
                        val responseType: Type = object : TypeToken<List<Responses>?>() {}.type
                        responsesList = gson.fromJson(responsesNotNull, responseType) as ArrayList<Responses>
                    }
                    questions?.let { questionsNotNull ->
                        val questionsAndOptionsType: Type = object : TypeToken<List<QuestionsAndOptions>?>() {}.type
                        val questionsAndOptions: ArrayList<QuestionsAndOptions> = gson.fromJson(questionsNotNull, questionsAndOptionsType)
                        val questionsAndOptionsWithPreviouslySelectedOptions = addPreviouslySelectedOptions(questionsAndOptions, responsesList)
                        viewModel.questionsAndOptions.value = questionsAndOptionsWithPreviouslySelectedOptions
                        viewModel.currentQuestionIndex.value = 0
                        createSubmitQuestionsList(questionsAndOptionsWithPreviouslySelectedOptions)
                        adapter.setData(questionsAndOptionsWithPreviouslySelectedOptions)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun addPreviouslySelectedOptions(questionsAndOptions: ArrayList<QuestionsAndOptions>, responsesList: ArrayList<Responses>): ArrayList<QuestionsAndOptions> {
        val list = ArrayList<QuestionsAndOptions>()
        for (i in 0 until questionsAndOptions.size step 1) {
            val questionAndOption = questionsAndOptions[i]
            val response = responsesList.filter { responseItem ->
                questionAndOption.questionId == responseItem.questionId
            }
            var selectedOption = ""
            if(response.isNotEmpty()){
                selectedOption = response[0].selectedOpt
            }
            questionAndOption.selectedOpt = selectedOption
           list.add(i,questionAndOption)
        }
        return list
    }

    private fun createSubmitQuestionsList(questionsAndOptions: List<QuestionsAndOptions>) {
        viewModel.submitAllQuestions.value = ArrayList()
        for (questionAndOption in questionsAndOptions) {
            val question = createSubmitQuestion(questionAndOption)
            viewModel.submitAllQuestions.value!!.add(question)
        }
    }

    private fun createSubmitQuestion(questionAndOption: QuestionsAndOptions): SubmitQuestionRequest {
        return SubmitQuestionRequest(
            courseId = questionAndOption.courseId!!,
            questionPaperId = viewModel.questionPaperInfo.questionPaperId,
            batchId = viewModel.questionPaperInfo.batchId,
            isAttempted = 1,
            studentId = viewModel.questionPaperInfo.studentId.toString(),
            questionId = questionAndOption.questionId!!,
            selectedOpt = questionAndOption.selectedOpt ?: "",
            trainerId = questionAndOption.trainerId!!,
            examId = viewModel.questionPaperInfo.examId
        )
    }

    private fun observeQuestionSubmitResponse() {
        viewModel.submitQuestionApiResponse.observe(this, Observer { commonResponse ->
            when (commonResponse.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    if (viewModel.isSubmitClicked) {
                        viewModel.submitAllQuestionsResponse()
                    } else {
                        updateCurrentQuestion()
                    }
                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun updateCurrentQuestion() {
        val shouldIncrement = viewModel.isNextQuestionClicked
        val currentIndex = viewModel.currentQuestionIndex.value
        if (shouldIncrement) {
            checkAndIncrementQuestionIndex(currentIndex)
        } else {
            checkAndDecrementQuestionIndex(currentIndex)
        }
    }

    private fun checkAndIncrementQuestionIndex(currentIndex: Int?) {
        currentIndex?.let { currentIndexNotNull ->
            if (currentIndexNotNull != viewModel.questionsAndOptions.value!!.size - 1) {
                viewModel.currentQuestionIndex.value = currentIndexNotNull + 1
                binding.rvQuestion.scrollToPosition(currentIndexNotNull + 1)
                adapter.updateUI(currentIndexNotNull + 1)
            }
        }
    }

    private fun checkAndDecrementQuestionIndex(currentIndex: Int?) {
        currentIndex?.let { currentIndexNotNull ->
            if (currentIndexNotNull != 0) {
                viewModel.currentQuestionIndex.value = currentIndexNotNull - 1
                binding.rvQuestion.scrollToPosition(currentIndexNotNull - 1)
                adapter.updateUI(currentIndexNotNull + 1)
            }
        }
    }

    private fun observeAllQuestionsSubmitResponse() {
        viewModel.submitAllQuestionsApiResponse.observe(this, Observer { commonResponse ->
            when (commonResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, "Uploaded Successfully")
                    finish()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    showAppToast(this, "Error in Upload")
                    when(commonResponse.code){
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
    }

    private fun setTimerText() {
        if (timer != null)
            timer!!.cancel()

        val totalDiff: Long = (viewModel.questionPaperInfo.duration * 60 * 1000).toLong() // duration is given in minutes
        timer = object : CountDownTimer((totalDiff) + 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60
                binding.actvDurationValue.text = String.format(
                    "%02d",
                    hours
                ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
                    "%02d",
                    seconds
                )
            }

            override fun onFinish() {
                submitFinalResponse()
            }
        }.start()
    }

    private fun setClickListeners() {
        setActivityBackClickListener()
        setQuestionBackClickListener()
        setNextClickListener()
        setSubmitClickListener()
    }

    private fun setActivityBackClickListener() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun setQuestionBackClickListener() {
        binding.btnBack.setOnClickListener {
            submitResponse(false, false)
        }
    }

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            submitResponse(true, false)
        }
    }

    private fun submitResponse(isNextButtonClicked: Boolean, isSubmitButtonClicked: Boolean) {
        updateResponseInQuestionsList()
        updateResponseInSubmitQuestionsList()
        viewModel.isNextQuestionClicked = isNextButtonClicked
        viewModel.isSubmitClicked = isSubmitButtonClicked
        val selectedOption = viewModel.currentQuestionAndOption.value?.selectedOpt
        viewModel.submitQuestionResponse(selectedOption)
    }

    private fun updateResponseInQuestionsList() {
        val question = viewModel.currentQuestionAndOption.value
        viewModel.questionsAndOptions.value?.set(viewModel.currentQuestionIndex.value!!, question!!)
    }

    private fun updateResponseInSubmitQuestionsList() {
        val question = viewModel.currentQuestionAndOption.value
        val submitQuestionRequest = createSubmitQuestion(question!!)
        viewModel.submitAllQuestions.value?.set(viewModel.currentQuestionIndex.value!!, submitQuestionRequest)
    }

    private fun setSubmitClickListener() {
        binding.btnSubmit.setOnClickListener {
            confirmUserBeforeSubmission()
        }
    }

    private fun confirmUserBeforeSubmission() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setTitle("You are about to submit the exam")
        alertDialogBuilder.setMessage("Are you sure you want to submit the exam ?")
        alertDialogBuilder.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
            submitFinalResponse()
        })
        alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        alertDialogBuilder.show()
    }

    private fun submitFinalResponse() {
        submitResponse(true, true)
    }

    companion object {
        const val KEY_QUESTION_PAPER_INFO = "QUESTION_PAPER_INFO"

        fun launchActivity(activity: Activity, questionPaperInfo: QuestionPaperListItemResData) {
            val intent = Intent(activity, QuestionPaperNewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(KEY_QUESTION_PAPER_INFO, questionPaperInfo)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}