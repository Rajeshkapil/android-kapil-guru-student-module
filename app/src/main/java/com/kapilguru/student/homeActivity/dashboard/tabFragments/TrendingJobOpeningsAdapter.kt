package com.kapilguru.student.homeActivity.dashboard.tabFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.AllTrendingWebinarsItemViewBinding
import com.kapilguru.student.databinding.LayoutJobOpeningsGridItemBinding
import com.kapilguru.student.databinding.LayoutJobOpeningsItemBinding
import com.kapilguru.student.databinding.TrendingWebinarsItemViewBinding
import com.kapilguru.student.experienceFormat
import com.kapilguru.student.homeActivity.models.AllWebinarsApi
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.salaryFormat
import kotlinx.android.synthetic.main.custom_key_value_view.view.*
import kotlinx.android.synthetic.main.layout_job_openings_grid_item.view.*

// isFrom false is DashBoard/TrendingWebinars,viewType-> 0
// isFrom true from AllTrendingWebinars, viewType-> 1
class TrendingJobOpeningsAdapter(var onItemClick: TrendingJobsCardClick, var isFrom: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var jobOpeningsDataList = listOf<JobOpeningsData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Holder(val itemTodayScheduleBinding: LayoutJobOpeningsGridItemBinding) : RecyclerView.ViewHolder(itemTodayScheduleBinding.root) {
        var view = itemTodayScheduleBinding


    }

    inner class AllTrendingHolder(val allitemTodayScheduleBinding: LayoutJobOpeningsGridItemBinding) : RecyclerView.ViewHolder(allitemTodayScheduleBinding.root) {
        var view = allitemTodayScheduleBinding

    }


    override fun getItemViewType(position: Int): Int = if (isFrom) 1 else 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutJobOpeningsGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(view)
        } else {
            val view = LayoutJobOpeningsGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AllTrendingHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as Holder).view.model = jobOpeningsDataList[position]
        } else {
            (holder as AllTrendingHolder).view.model = jobOpeningsDataList[position]
        }
        val data: JobOpeningsData = jobOpeningsDataList[position]
        holder.itemView.keyValueText.text_value.text = experienceFormat(data.minExp, data.maxExp)
        holder.itemView.keyValueText2.text_value.text = salaryFormat(data.minSalary, data.maxSalary)
        holder.itemView.cardViewJob.setOnClickListener {
            onItemClick.onTrendingJobCardClick(jobOpeningsDataList[position])
        }
    }

    override fun getItemCount() = jobOpeningsDataList.size


    interface TrendingJobsCardClick {
        fun onTrendingJobCardClick(jobOpeningsData: JobOpeningsData)
    }


}
