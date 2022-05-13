package com.kapilguru.student.homeActivity.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.TopCategoriesItemViewBinding
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import java.util.*


class TodayCategoriesAdapter(var onItemClick: TopCategoriesCardClick) : RecyclerView.Adapter<TodayCategoriesAdapter.Holder>() {

    var upComingScheduleApiList = listOf<TopCategoriesApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: com.kapilguru.student.databinding.TopCategoriesItemViewBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding

        init {
            view.root.setOnClickListener {
                onItemClick.onTopCategoriesCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = TopCategoriesItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemTodayScheduleBinding.model = upComingScheduleApiList[position]
    }

    override fun getItemCount() = upComingScheduleApiList.size

    interface TopCategoriesCardClick {
        fun onTopCategoriesCardClick(cateogry: TopCategoriesApi)
    }

}
