package com.kapilguru.student.exam.questionPaper

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.R
import com.kapilguru.student.databinding.QuestionsListItemBinding
import com.kapilguru.student.exam.model.QuestionsAndOptions
import com.kapilguru.student.exam.questionPaper.viewModel.QuestionPaperViewModel
import com.kapilguru.student.fromBase64ToString

class QuestionsListAdapter(val viewModel: QuestionPaperViewModel) : RecyclerView.Adapter<QuestionsListAdapter.QuestionViewHolder>() {
    private val TAG = "QuestionsListAdapter"
    private var mQuestionsList = ArrayList<QuestionsAndOptions>()

    fun setData(questionsList: ArrayList<QuestionsAndOptions>) {
        mQuestionsList = questionsList
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(val binding: QuestionsListItemBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            val context = binding.root.context
            binding.rbOptionA.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    viewModel.currentQuestionAndOption.value?.selectedOpt = context.getString(R.string.option1)
                }
            }
            binding.rbOptionB.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    viewModel.currentQuestionAndOption.value?.selectedOpt = context.getString(R.string.option2)
                }
            }
            binding.rbOptionC.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    viewModel.currentQuestionAndOption.value?.selectedOpt = context.getString(R.string.option3)
                }
            }
            binding.rbOptionD.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    viewModel.currentQuestionAndOption.value?.selectedOpt = context.getString(R.string.option4)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = QuestionsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.binding.viewModel = viewModel
        setQuestionNumber(holder, position)
        setSelectedOption(holder)
    }

    fun updateUI(position: Int){
        notifyItemChanged(position)
    }

    private fun setQuestionNumber(holder: QuestionViewHolder, position: Int) {
        val totalQuestions = viewModel.questionsAndOptions.value!!.size
        holder.binding.actvQuestionNo.text = java.lang.String.format(holder.binding.root.context.getString(R.string.question_no), position + 1, totalQuestions)
    }

    private fun setSelectedOption(holder: QuestionViewHolder){
        val questionsAndOptionsNotNull =  viewModel.currentQuestionAndOption.value!!
        val context = holder.binding.root.context
        when (questionsAndOptionsNotNull.selectedOpt) {
            context.getString(R.string.option1) -> setRadioButtonChecked(holder.binding.rbOptionA)
            context.getString(R.string.option2) -> setRadioButtonChecked(holder.binding.rbOptionB)
            context.getString(R.string.option3) -> setRadioButtonChecked(holder.binding.rbOptionC)
            context.getString(R.string.option4) -> setRadioButtonChecked(holder.binding.rbOptionD)
            else -> clearSelectedOptions(holder)
        }
    }

    private fun clearSelectedOptions(holder: QuestionViewHolder) {
        holder.binding.rbOptionA.isChecked = false
        holder.binding.rbOptionB.isChecked = false
        holder.binding.rbOptionC.isChecked = false
        holder.binding.rbOptionD.isChecked = false
    }

    private fun setRadioButtonChecked(radioButton : RadioButton){
       /* binding.root.post(object : Runnable{
            override fun run() {
//                binding.rgOptions.check(radioButton.id)
                radioButton.isChecked = true
            }

        })*/
        radioButton.isChecked = true
    }

    override fun getItemCount(): Int {
        return mQuestionsList.size
    }
}