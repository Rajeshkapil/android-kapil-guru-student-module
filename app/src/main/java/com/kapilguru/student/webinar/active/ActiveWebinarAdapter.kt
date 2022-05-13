package com.kapilguru.student.webinar.active

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemActiveWebinarBinding
import com.kapilguru.student.webinar.model.ActiveWebinar

class ActiveWebinarAdapter(val mListener: ActiveWebinarClickListener) : RecyclerView.Adapter<ActiveWebinarAdapter.BatchViewHolder>() {
    private var activeModelList = ArrayList<ActiveWebinar>()

    fun setData(activeWebinarList: ArrayList<ActiveWebinar>) {
        activeModelList = activeWebinarList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val binding = ItemActiveWebinarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.binding.model = activeModelList[position]
    }

    override fun getItemCount(): Int {
        return activeModelList.size
    }

    inner class BatchViewHolder(val binding: ItemActiveWebinarBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.acivShareIcon.setOnClickListener {
                mListener.onShareClicked(activeModelList[bindingAdapterPosition])
            }

            binding.root.setOnClickListener {
                mListener.onViewMoreClicked(activeModelList[bindingAdapterPosition])
            }
        }
    }

    interface ActiveWebinarClickListener {
        fun onShareClicked(activeWebinarData: ActiveWebinar)
        fun onViewMoreClicked(activeWebinarData: ActiveWebinar)
    }
}