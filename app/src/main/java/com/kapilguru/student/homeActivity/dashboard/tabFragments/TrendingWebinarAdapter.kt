package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.BuildConfig
import com.kapilguru.student.DEMO_LECTURES_DETAILS
import com.kapilguru.student.databinding.AllTrendingWebinarsItemViewBinding
import com.kapilguru.student.databinding.TrendingWebinarsItemViewBinding
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.homeActivity.models.AllWebinarsApi
import com.kapilguru.student.shareIntent
import com.kapilguru.student.ui.profile.OTPDialogFragment.Companion.TAG

// isFrom false is DashBoard/TrendingWebinars,viewType-> 0
// isFrom true from AllTrendingWebinars, viewType-> 1
class TrendingWebinarAdapter(var onItemClick: TrendingWebinarCardClick, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var upComingScheduleApiList = listOf<AllWebinarsApi>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: TrendingWebinarsItemViewBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding
        init {
            view.card.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }

    }

    inner class AllTrendingHolder(val allitemTodayScheduleBinding: AllTrendingWebinarsItemViewBinding) : RecyclerView.ViewHolder(allitemTodayScheduleBinding.root) {
        var view = allitemTodayScheduleBinding
        init {
            view.root.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.btnKnowMore.setOnClickListener {
                onItemClick.onTrendingWebinarCardClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
            view.shareIcon.setOnClickListener {
                onItemClick.onShareClick(upComingScheduleApiList[absoluteAdapterPosition])
            }
        }
    }


    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = TrendingWebinarsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(view)
        } else {
            val view = AllTrendingWebinarsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun getItemCount(): Int {
        return upComingScheduleApiList.size
    }


    interface TrendingWebinarCardClick {
        fun onTrendingWebinarCardClick(webinarDetails: AllWebinarsApi)
        fun onShareClick(webinarDetails: AllWebinarsApi)
    }


}
