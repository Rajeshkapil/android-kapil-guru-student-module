package com.kapilguru.student.exam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.NETWORK_CONNECTIVITY_EROR
import com.kapilguru.student.PARAM_QUESTIONS_REQUEST
import com.kapilguru.student.databinding.FragmentViewAnswerSheetBinding
import com.kapilguru.student.exam.model.*
import com.kapilguru.student.network.Status
import com.kapilguru.student.networkConnectionErrorDialog
import java.lang.reflect.Type

class ViewAnswerSheetFragment : Fragment(),QuestionClickListener {

    lateinit var binding: FragmentViewAnswerSheetBinding
    val viewModel: ExamViewModel by activityViewModels()
    lateinit var progressDialog: CustomProgressDialog
    private val TAG = "ViewAnswerSheetFragment"
    lateinit var studentQuestionRecycler: StudentQuestionRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewAnswerSheetBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(this.requireActivity())
        setUpRecycler()
        apiCall()
        observeViewModel()
    }

    private fun setUpRecycler() {
        studentQuestionRecycler = StudentQuestionRecycler(this)
        binding.questionsRecycler.adapter = studentQuestionRecycler
    }

    private fun apiCall() {
        val studentReportRequest = arguments?.getParcelable<QuestionsRequest>(PARAM_QUESTIONS_REQUEST)
        studentReportRequest?.let {
            viewModel.getQuestionsRequest(it)
        }
    }


    private fun observeViewModel() {
        viewModel.questionsReponse.observe(this.viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressDialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val questions = response.data?.questionsApi?.questions
                    val responses = response.data?.questionsApi?.responses
                    var gson = Gson()
                    questions?.let { it ->
                        val questionsAndOptionsType: Type =
                            object : TypeToken<List<QuestionsAndOptions>?>() {}.type
                        val questionsAndOptions: List<QuestionsAndOptions> = gson.fromJson(questions.toString(), questionsAndOptionsType)
                        questionsAndOptions.let { questionsAndOptions ->
                            viewModel.questionsAndOptions.value = questionsAndOptions as ArrayList<QuestionsAndOptions>
                        }
                    }

                    gson = Gson()
                    responses?.let { it ->
                        val selectedOptionType: Type =
                            object : TypeToken<List<SelectedOption>?>() {}.type
                        val selectedOption: List<SelectedOption> = gson.fromJson(responses.toString(), selectedOptionType)
                        selectedOption.let { selectedOption ->
                            viewModel.selectedOption.value = selectedOption as ArrayList<SelectedOption>
                        }
                    }
                    // merge questions and selceted ans
                    updateQuestionAndOptionsWithSelectedOptions()
                    updateRecyclerView()
                    showDefaultData()
                    progressDialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    progressDialog.dismissLoadingDialog()
                    when(response.code) {
                        NETWORK_CONNECTIVITY_EROR -> networkConnectionErrorDialog(requireContext())
                    }
//                    if (response.data?.status != 200) {
//
//                    } else {
//
//                    }
                }
            }
        })
    }

    private fun showDefaultData() {
        viewModel.questionsAndOptions.value?.let {
            if(it.size>0)
            onQuestionClicked(it[0],1)
        }
    }

    private fun updateRecyclerView() {
        studentQuestionRecycler.questionPaperResponse = viewModel.questionsAndOptions.value as ArrayList<QuestionsAndOptions>
    }

    private fun updateQuestionAndOptionsWithSelectedOptions() {
        viewModel.questionsAndOptions.value?.forEachIndexed { questionIndex, questionsAndOptions ->
            viewModel.selectedOption.value?.forEachIndexed { selectedOptionIndex, selectedOption ->
                if(questionsAndOptions.questionId == selectedOption.questionId){
                    selectedOption.selectedOpt?.let {
                        if(it.contains("\"")) {
                            viewModel.questionsAndOptions.value!![questionIndex].selectedOpt = selectedOption.selectedOpt.toString().replace("\"", "").trim()
                        } else {
                            viewModel.questionsAndOptions.value!![questionIndex].selectedOpt = selectedOption.selectedOpt?.trim()
                        }
                    }
                    viewModel.questionsAndOptions.value!![questionIndex].correctOpt = selectedOption.correctOpt.toString()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(questionsRequest: QuestionsRequest?) =
            ViewAnswerSheetFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PARAM_QUESTIONS_REQUEST, questionsRequest)
                    }
            }
    }

    override fun onQuestionClicked(questionsAndOptions: QuestionsAndOptions, position: Int) {
        binding.selectedPosition = position
        viewModel.selectedQuestionAndOption.value = questionsAndOptions
    }
}