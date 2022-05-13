package com.kapilguru.student.topCategories.selectedTopCategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemCourseBySelectedCategoryBinding
import com.kapilguru.student.topCategories.selectedTopCategory.model.SelectedTopCategoryCourseResData

class SelectedTopCategoryCourseAdapter(val listener : CourseClickListener) : RecyclerView.Adapter<SelectedTopCategoryCourseAdapter.ViewHolder>() {
    private var mCourseList = ArrayList<SelectedTopCategoryCourseResData>()

    fun setData(courseList : ArrayList<SelectedTopCategoryCourseResData>){
        mCourseList  = courseList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : ItemCourseBySelectedCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                listener.onCourseClicked(mCourseList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourseBySelectedCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mCourseList[position]
    }

    override fun getItemCount(): Int {
        return mCourseList.size
    }

    interface CourseClickListener{
        fun onCourseClicked(course : SelectedTopCategoryCourseResData)
    }
}