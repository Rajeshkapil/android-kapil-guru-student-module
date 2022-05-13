package com.kapilguru.student.exam.questionPaper

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityQuestionPaperBinding
import com.kapilguru.student.exam.model.QuestionsAndOptions
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.SubmitQuestionRequest
import com.kapilguru.student.exam.questionPaper.viewModel.QuestionPaperViewModel
import com.kapilguru.student.exam.questionPaper.viewModel.QuestionPaperViewModelFactory
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import java.lang.reflect.Type

class QuestionPaperActivity : AppCompatActivity() {
    private val TAG = "QuestionPaperActivity"
    private lateinit var binding: ActivityQuestionPaperBinding
    private lateinit var viewModel: QuestionPaperViewModel
    private lateinit var progressDialog: CustomProgressDialog
    var timer: CountDownTimer? = null

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_paper)
        viewModel = ViewModelProvider(this, QuestionPaperViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(QuestionPaperViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        progressDialog = CustomProgressDialog(this)
    }

    private fun getIntentData() {
        val questionPaperInfo = intent.getParcelableExtra<QuestionPaperListItemResData>(KEY_QUESTION_PAPER_INFO)
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
        observeQuestionsApiResponse()
        observeCurrentQuestionIndex()
        observeCurrentQuestion()
        observeQuestionSubmitResponse()
        observeAllQuestionsSubmitResponse()
    }

    private fun observeQuestionsApiResponse() {
        viewModel.questionsResponse.observe(this, Observer { questionsApiResponse ->
            when (questionsApiResponse.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }
                Status.SUCCESS -> {
                    val questions = questionsApiResponse.data?.questionsApi?.questions
                    val gson = Gson()
                    questions?.let { questionsNotNull ->
                        val questionsAndOptionsType: Type =
                            object : TypeToken<List<QuestionsAndOptions>?>() {}.type
                        val questionsAndOptions: List<QuestionsAndOptions> = gson.fromJson(questionsNotNull, questionsAndOptionsType)
                        viewModel.questionsAndOptions.value = questionsAndOptions as ArrayList<QuestionsAndOptions>
                        viewModel.currentQuestionIndex.value = 0
                        createSubmitQuestionsList(questionsAndOptions)
                    }
                    progressDialog.dismissLoadingDialog()
                }
                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(questionsApiResponse.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(this)
                    }
                }
            }
        })
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

    private fun observeCurrentQuestionIndex() {
        viewModel.currentQuestionIndex.observe(this, Observer { questionPaperIndex ->
            setQuestion(questionPaperIndex)
            setQuestionNumber(questionPaperIndex)
            setActionButtonsVisibility(questionPaperIndex)
        })
    }

    private fun setQuestion(questionPaperIndex: Int) {
        viewModel.currentQuestionAndOption.value = viewModel.questionsAndOptions.value!![questionPaperIndex]
    }

    private fun setQuestionNumber(questionPaperIndex: Int) {
        val totalQuestions = viewModel.questionsAndOptions.value!!.size
        binding.actvQuestionNo.text = java.lang.String.format(getString(R.string.question_no), questionPaperIndex + 1, totalQuestions)
    }

    private fun setActionButtonsVisibility(questionPaperIndex: Int) {
        val lastQuestionIndex = viewModel.questionsAndOptions.value!!.size - 1
        if (questionPaperIndex == 0) {
            setBackBtnProperties(true, false)
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

    private fun observeCurrentQuestion() {
        viewModel.currentQuestionAndOption.observe(this, Observer { questionPaper ->
            setSelectedOption(questionPaper)
//            binding.currentQuestionAndOptions = questionPaper
        })
    }

    private fun setSelectedOption(questionsAndOptions: QuestionsAndOptions?) {
        questionsAndOptions?.let { questionsAndOptionsNotNull ->
//            clearSelectedOptions()
            Log.d(TAG, "setSelectedOption: questionsAndOptionsNotNull.selectedOpt : " + questionsAndOptionsNotNull.selectedOpt)
            when (questionsAndOptionsNotNull.selectedOpt) {
                "" -> clearSelectedOptions()
                null -> clearSelectedOptions()
               /* getString(R.string.option1) -> binding.rbOptionA.isChecked = true
                getString(R.string.option2) -> binding.rbOptionB.isChecked = true
                getString(R.string.option3) -> binding.rbOptionC.isChecked = true
                getString(R.string.option4) -> binding.rbOptionD.isChecked = true*/
               /* getString(R.string.option1) -> binding.rgOptions.check(R.id.rb_option_a)
                getString(R.string.option2) -> binding.rgOptions.check(R.id.rb_option_b)
                getString(R.string.option3) -> binding.rgOptions.check(R.id.rb_option_c)
                getString(R.string.option4) -> binding.rgOptions.check(R.id.rb_option_d)*/
               /* getString(R.string.option1) -> (binding.rgOptions.getChildAt(0) as RadioButton).isChecked = true
                getString(R.string.option2) -> (binding.rgOptions.getChildAt(1) as RadioButton).isChecked = true
                getString(R.string.option3) -> (binding.rgOptions.getChildAt(2) as RadioButton).isChecked = true
                getString(R.string.option4) -> (binding.rgOptions.getChildAt(3) as RadioButton).isChecked = true*/
                getString(R.string.option1) -> setRadioButtonChecked(binding.rbOptionA)
                getString(R.string.option2) -> setRadioButtonChecked(binding.rbOptionB)
                getString(R.string.option3) -> setRadioButtonChecked(binding.rbOptionC)
                getString(R.string.option4) -> setRadioButtonChecked(binding.rbOptionD)
                else -> Log.d(TAG, "setSelectedOption: else in when")
            }
        }
    }

    private fun setRadioButtonChecked(radioButton : RadioButton){
        binding.root.post(object : Runnable{
            override fun run() {
//                binding.rgOptions.check(radioButton.id)
                radioButton.isChecked = true
            }

        })
    }

    private fun clearSelectedOptions() {
        binding.rbOptionA.isChecked = false
        binding.rbOptionB.isChecked = false
        binding.rbOptionC.isChecked = false
        binding.rbOptionD.isChecked = false
    }

    private fun observeQuestionSubmitResponse() {
        viewModel.submitQuestionApiResponse.observe(this, Observer { commonResponse ->
            when (commonResponse.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    updateCurrentQuestion()
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
            }
        }
    }

    private fun checkAndDecrementQuestionIndex(currentIndex: Int?) {
        currentIndex?.let { currentIndexNotNull ->
            if (currentIndexNotNull != 0) {
                viewModel.currentQuestionIndex.value = currentIndexNotNull - 1
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

    private fun setTimerText(){
        if (timer != null)
            timer!!.cancel()

        val totalDiff : Long = (viewModel.questionPaperInfo.duration*60*1000).toLong() // duration is given in minutes
        timer =  object : CountDownTimer((totalDiff) + 1000 , 1000) {
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
        setRadioButtonCheckListener()
        setActivityBackClickListener()
        setRadioButtonCheckListener()
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
            submitResponse(false)
        }
    }

    private fun setNextClickListener() {
        binding.btnNext.setOnClickListener {
            submitResponse(true)
        }
    }

    private fun submitResponse(isNextButtonClicked: Boolean) {
        updateResponseInQuestionsList()
        updateResponseInSubmitQuestionsList()
        viewModel.isNextQuestionClicked = isNextButtonClicked
        val selectedOption = getSelectedOption()
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

    private fun getSelectedOption(): String {
        return when {
            binding.rbOptionA.isChecked -> getString(R.string.option1)
            binding.rbOptionB.isChecked -> getString(R.string.option2)
            binding.rbOptionC.isChecked -> getString(R.string.option3)
            binding.rbOptionD.isChecked -> getString(R.string.option4)
            else -> ""
        }
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
        viewModel.submitAllQuestionsResponse()
    }

    private fun setRadioButtonCheckListener() {
        binding.rbOptionA.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.currentQuestionAndOption.value?.selectedOpt = getString(R.string.option1)
                Log.d(TAG, "setRadioButtonCheckListener: option a selected")
            }else{
                Log.d(TAG, "setRadioButtonCheckListener: option a un selected")
            }
        }
        binding.rbOptionB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.currentQuestionAndOption.value?.selectedOpt = getString(R.string.option2)
                Log.d(TAG, "setRadioButtonCheckListener: option b selected")
            }else{
                Log.d(TAG, "setRadioButtonCheckListener: option b un selected")
            }
        }
        binding.rbOptionC.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.currentQuestionAndOption.value?.selectedOpt = getString(R.string.option3)
                Log.d(TAG, "setRadioButtonCheckListener: option c selected")
            }else{
                Log.d(TAG, "setRadioButtonCheckListener: option c un selected")
            }
        }
        binding.rbOptionD.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.currentQuestionAndOption.value?.selectedOpt = getString(R.string.option4)
                Log.d(TAG, "setRadioButtonCheckListener: option d selected")
            }else{
                Log.d(TAG, "setRadioButtonCheckListener: option d un selected")
            }
        }
    }

    companion object {
        const val KEY_QUESTION_PAPER_INFO = "QUESTION_PAPER_INFO"

        fun launchActivity(activity: Activity, questionPaperInfo: QuestionPaperListItemResData) {
            val intent = Intent(activity, QuestionPaperActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(KEY_QUESTION_PAPER_INFO, questionPaperInfo)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
}