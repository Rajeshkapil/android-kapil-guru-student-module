package com.kapilguru.student.courseDetails.batchList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.R
import com.kapilguru.student.addTwoDays
import com.kapilguru.student.calculateDateDiff
import com.kapilguru.student.courseDetails.model.BatchesItem
import com.kapilguru.student.databinding.BatchItemBinding
import com.kapilguru.student.isGreaterThanCurrentDate
import java.util.*


class BatchAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<BatchAdapter.Holder>() {

    var item = listOf<BatchesItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val batchItemBinding: BatchItemBinding) : RecyclerView.ViewHolder(batchItemBinding.root) {
        var view = batchItemBinding

        init {
            view.mainView.setOnClickListener {
                onItemClick.onCardClick(item[absoluteAdapterPosition])
            }
            view.btnEnroll.setOnClickListener {
                onItemClick.onCardClick(item[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = BatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = item[position]
        holder.batchItemBinding.model = currentItem
       val graceStartDate =  currentItem.startDate
        val startDateCalendar = graceStartDate?.addTwoDays()
        val todayDate = Calendar.getInstance()
        todayDate.timeZone = TimeZone.getTimeZone("IST")
        val endDateDiff = startDateCalendar?.calculateDateDiff(todayDate)
        if(currentItem.currentNoOfStudents == currentItem.maxNoOfStudents ||
            todayDate.isGreaterThanCurrentDate(startDateCalendar!!)){
            holder.view.actvStatus.text = "Batch Full"
            holder.view.actvStatus.setTextColor(ContextCompat.getColor(holder.view.actvStatus.context, R.color.red))
        } else if (((currentItem.currentNoOfStudents.toFloat()/currentItem.maxNoOfStudents)*100)<50) {
            holder.view.actvStatus.text = "Filling Fast"
            holder.view.actvStatus.setTextColor(ContextCompat.getColor(holder.view.actvStatus.context, R.color.green_2))
        } else if (((currentItem.currentNoOfStudents.toFloat()/currentItem.maxNoOfStudents)*100)>=50) {
            holder.view.actvStatus.text = "Hurry Up"
            holder.view.actvStatus.setTextColor(ContextCompat.getColor(holder.view.actvStatus.context, R.color.red))
        }

    }

    override fun getItemCount() = item.size


    interface OnItemClick {
        fun onCardClick(batchItem: BatchesItem)
    }

}
