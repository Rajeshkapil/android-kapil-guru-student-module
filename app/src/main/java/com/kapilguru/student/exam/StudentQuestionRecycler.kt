package com.kapilguru.student.exam


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.StudentQuestionRecyclerBinding
import com.kapilguru.student.exam.model.QuestionsAndOptions

class StudentQuestionRecycler(val questionClickListener: QuestionClickListener) : RecyclerView.Adapter<StudentQuestionRecycler.Holder>() {

    var questionPaperResponse = arrayListOf<QuestionsAndOptions>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var previousSelectedValue = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = StudentQuestionRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.position = position+1
        holder.view.model = questionPaperResponse[position]
    }

    override fun getItemCount(): Int = questionPaperResponse.size

    inner class Holder(itemView: StudentQuestionRecyclerBinding) : RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var btn = itemView.positionNumber
        init {
            btn.setOnClickListener {
                if(previousSelectedValue==-1) {
                    previousSelectedValue = absoluteAdapterPosition
                    questionPaperResponse[previousSelectedValue].userSelection?.let {it->
                        it.value?.let { newInfo->
                            questionPaperResponse[previousSelectedValue].userSelection.value = !newInfo
                        }

                    }
                } else {
                     questionPaperResponse[previousSelectedValue].userSelection?.let {it->
                        it.value?.let { newInfo->
                            questionPaperResponse[previousSelectedValue].userSelection.value = !newInfo
                        }

                    }
                    previousSelectedValue = absoluteAdapterPosition
                    questionPaperResponse[previousSelectedValue].userSelection?.let {it->
                        it.value?.let { newInfo->
                            questionPaperResponse[previousSelectedValue].userSelection.value = !newInfo
                        }

                    }
                }
                questionClickListener.onQuestionClicked(questionPaperResponse[absoluteAdapterPosition],position = absoluteAdapterPosition+1)
            }
        }
    }

}

interface QuestionClickListener {
     fun onQuestionClicked(questionsAndOptions: QuestionsAndOptions, position:Int)
}