package com.kapilguru.student.courseDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.courseDetails.model.Training
import com.kapilguru.student.databinding.TrainingItemBinding

class TrainingAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<TrainingAdapter.Holder>() {

    var item = listOf<Training>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(trainingItemBinding: TrainingItemBinding) : RecyclerView.ViewHolder(trainingItemBinding.root) {
        var view = trainingItemBinding

        init {
//            view.card.setOnClickListener {
//                onItemClick.onCardClick(upComingScheduleApiList[absoluteAdapterPosition])
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = TrainingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = item[position]
    }

    override fun getItemCount() = item.size


    interface OnItemClick {
        fun onCardClick(batchItem: Training)
    }

}
