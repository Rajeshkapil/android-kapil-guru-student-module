package com.kapilguru.student.homeActivity.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemTodayScheduleBinding
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi


class TodayScheduleAdapter(var onItemClick: OnItemClick) : RecyclerView.Adapter<TodayScheduleAdapter.Holder>() {

    var upComingScheduleApiList = listOf<UpComingScheduleApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: ItemTodayScheduleBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding

        init {
            view.card.setOnClickListener {
                onItemClick.onCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ItemTodayScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemTodayScheduleBinding.model = upComingScheduleApiList[position]
    }

    override fun getItemCount() = upComingScheduleApiList.size


    interface OnItemClick {
        fun onCardClick(upComingScheduleApi: UpComingScheduleApi)
    }

}
