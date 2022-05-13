package com.kapilguru.student.completionRequest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.completionRequest.model.CompletionRequestResData
import com.kapilguru.student.databinding.ItemCompletionRequestBinding

class CompletionRequestAdapter(val studentName: String, val listener: CompletionRequestListener) : RecyclerView.Adapter<CompletionRequestAdapter.ViewHolder>() {
    private var mCompletionRequestList = ArrayList<CompletionRequestResData>()

    fun setData(completionRequest: ArrayList<CompletionRequestResData>) {
        mCompletionRequestList = completionRequest
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompletionRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mCompletionRequestList[position]
        holder.binding.studentName = studentName
    }

    override fun getItemCount(): Int {
        return mCompletionRequestList.size
    }

    inner class ViewHolder(val binding: ItemCompletionRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnAccept.setOnClickListener {
                listener.onAcceptClicked(mCompletionRequestList[bindingAdapterPosition])
            }
            binding.btnReject.setOnClickListener {
                listener.onRejectClicked(mCompletionRequestList[bindingAdapterPosition])
            }
        }
    }

    interface CompletionRequestListener {
        fun onRejectClicked(completionRequest: CompletionRequestResData)
        fun onAcceptClicked(completionRequest: CompletionRequestResData)
    }
}