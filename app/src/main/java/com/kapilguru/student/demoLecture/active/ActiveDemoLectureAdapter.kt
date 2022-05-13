package com.kapilguru.student.demoLecture.active

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemActiveDemolectureBinding
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures

class ActiveDemoLectureAdapter(val mListener: ActiveDemoLecturesClickListener) :
    RecyclerView.Adapter<ActiveDemoLectureAdapter.ViewHolder>() {
    private var activeDemoLectureList = ArrayList<ActiveDemoLectures>()

    fun setData(activeDemoLectureList: ArrayList<ActiveDemoLectures>) {
        this.activeDemoLectureList = activeDemoLectureList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActiveDemolectureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = activeDemoLectureList[position]
    }

    override fun getItemCount(): Int {
        return activeDemoLectureList.size
    }

    inner class ViewHolder(val binding: ItemActiveDemolectureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.acivShareIcon.setOnClickListener {
                mListener.onShareClicked(activeDemoLectureList[bindingAdapterPosition])
            }

            binding.root.setOnClickListener {
                mListener.onViewMoreClicked(activeDemoLectureList[bindingAdapterPosition])
            }

        }
    }

    interface ActiveDemoLecturesClickListener {
        fun onShareClicked(activeData: ActiveDemoLectures)
        fun onViewMoreClicked(activeData: ActiveDemoLectures)
    }
}