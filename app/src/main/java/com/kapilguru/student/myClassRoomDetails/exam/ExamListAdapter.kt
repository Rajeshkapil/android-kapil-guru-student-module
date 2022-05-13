package com.kapilguru.student.myClassRoomDetails.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemExamListBinding
import com.kapilguru.student.myClassRoomDetails.exam.model.QuestionPaperListItemResData

class ExamListAdapter(val mListener: ExamItemClickListener) : RecyclerView.Adapter<ExamListAdapter.ViewHolder>() {
    private var mExamList = ArrayList<QuestionPaperListItemResData>()
    private var mBatchCode = ""
    fun setData(batchCode : String?,examList: ArrayList<QuestionPaperListItemResData>) {
        mExamList = examList
//        mBatchCode = "Batch Code - "+batchCode
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemExamListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val questionPaper = mExamList[bindingAdapterPosition]
                if(questionPaper.isCompleted == 1){
                    mListener.onViewResultClicked(questionPaper)
                }else{
                    mListener.onStartExamClicked(questionPaper)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mExamList[position]
//        holder.binding.actvBatchCode.text = mBatchCode
    }

    override fun getItemCount(): Int {
        return mExamList.size
    }

    interface ExamItemClickListener {
        fun onStartExamClicked(questionPaper: QuestionPaperListItemResData)
        fun onViewResultClicked(questionPaper: QuestionPaperListItemResData)
    }
}