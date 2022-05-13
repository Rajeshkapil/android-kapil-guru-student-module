package com.kapilguru.student.jobOpenings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemActiveDemolectureBinding
import com.kapilguru.student.databinding.LayoutJobOpeningsItemBinding
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures
import com.kapilguru.student.experienceFormat
import com.kapilguru.student.jobOpenings.model.JobOpeningsData
import com.kapilguru.student.jobOpenings.model.JobOpeningsResponse
import com.kapilguru.student.salaryFormat
import kotlinx.android.synthetic.main.custom_key_value_view.view.*
import kotlinx.android.synthetic.main.layout_job_openings_grid_item.view.*

class JobOpeningsAdapter(val mListener: JobOpeningsClickListener) :
    RecyclerView.Adapter<JobOpeningsAdapter.ViewHolder>() {
    private var jobOpeningsDataList = ArrayList<JobOpeningsData>()

    fun setData(activeDemoLectureList: ArrayList<JobOpeningsData>) {
        this.jobOpeningsDataList = activeDemoLectureList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutJobOpeningsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: JobOpeningsData = jobOpeningsDataList[position]
        data.let {
            holder.binding.model = it
            holder.binding.keyValueText.text_value.text = experienceFormat(it.minExp, it.maxExp)
            holder.binding.keyValueText2.text_value.text = salaryFormat(it.minSalary, it.maxSalary)
            holder.binding.root.setOnClickListener {
                mListener.onItemClicked(data)
            }
        }

    }

    override fun getItemCount(): Int {
        return jobOpeningsDataList.size
    }

    inner class ViewHolder(val binding: LayoutJobOpeningsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    interface JobOpeningsClickListener {
        fun onItemClicked(jobOpeningsData: JobOpeningsData)
    }
}