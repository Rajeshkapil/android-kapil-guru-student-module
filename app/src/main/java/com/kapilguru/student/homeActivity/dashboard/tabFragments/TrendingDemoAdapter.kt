package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.AllTrendingDemosItemViewBinding
import com.kapilguru.student.databinding.AllTrendingWebinarsItemViewBinding
import com.kapilguru.student.databinding.TrendingDemosItemViewBinding
import com.kapilguru.student.databinding.TrendingWebinarsItemViewBinding
import com.kapilguru.student.homeActivity.models.AllDemosApi
import com.kapilguru.student.homeActivity.models.AllWebinarsApi

// isFrom false is DashBoard/TrendingDemos,viewType-> 0
// isFrom true from AllTrendingDemos, viewType-> 1
class TrendingDemoAdapter(var onItemClick: TrendingDemosCardClick, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var upComingScheduleApiList = listOf<AllDemosApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: TrendingDemosItemViewBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding
        init {
            view.card.setOnClickListener {
                onItemClick.onTrendingDemosCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingDemosCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }

    }

    inner class AllTrendingHolder(val allitemTodayScheduleBinding: AllTrendingDemosItemViewBinding) : RecyclerView.ViewHolder(allitemTodayScheduleBinding.root) {
        var view = allitemTodayScheduleBinding
        init {
            view.card.setOnClickListener {
                onItemClick.onTrendingDemosCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingDemosCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }

    }


    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = TrendingDemosItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(view)
        } else {
            val view = AllTrendingDemosItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllTrendingHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as Holder).view.model = upComingScheduleApiList[position]
        } else {
            (holder as AllTrendingHolder).view.model = upComingScheduleApiList[position]
        }
    }

    override fun getItemCount() = upComingScheduleApiList.size


    interface TrendingDemosCardClick {
        fun onTrendingDemosCardClick(upComingScheduleApi: AllDemosApi)
        fun onShareClick(demosDetails: AllDemosApi)
    }


}
