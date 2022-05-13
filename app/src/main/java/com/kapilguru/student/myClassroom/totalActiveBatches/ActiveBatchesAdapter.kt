package com.kapilguru.student.myClassroom.totalActiveBatches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemActiveBatchesBinding
import com.kapilguru.student.myClassroom.liveClasses.model.ActiveBatchData

class ActiveBatchesAdapter(val mListener: BatchClickListener) :
    RecyclerView.Adapter<ActiveBatchesAdapter.BatchViewHolder>() {
    private var mBatchList = ArrayList<ActiveBatchData>()

    fun setData(batchList: ArrayList<ActiveBatchData>) {
        mBatchList = batchList
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(val binding: ItemActiveBatchesBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actvOverview.setOnClickListener {
                mListener.onOverViewClicked(mBatchList[bindingAdapterPosition])
            }
            binding.studentDetails.setOnClickListener {
                mListener.onOverViewClicked(mBatchList[bindingAdapterPosition])
            }

            binding.activeRecordings.setOnClickListener {
//                mListener.onRecordingsClicked(mLiveUpComingClassList[bindingAdapterPosition])
            }

            binding.activeStudyMaterial.setOnClickListener {
                mListener.onStudyMaterialClicked(mBatchList[bindingAdapterPosition])
            }

            binding.activeExam.setOnClickListener {
                mListener.onExamClicked(mBatchList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding =
            ItemActiveBatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.binding.model = mBatchList[position]
        holder.binding.root.setOnClickListener {
            mListener.onBatchClicked(mBatchList[position])
        }
    }

    override fun getItemCount(): Int {
        return mBatchList.size
    }

    interface BatchClickListener {
        fun onBatchClicked(batchData: ActiveBatchData)
        fun onOverViewClicked(batchData: ActiveBatchData)
        fun onStudyMaterialClicked(batchData: ActiveBatchData)
        fun onExamClicked(batchData: ActiveBatchData)
        fun onCompletionRequestClicked(batchData: ActiveBatchData)
    }
}