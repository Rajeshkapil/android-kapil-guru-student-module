package com.kapilguru.student.myClassRoomDetails.studymaterial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemTopCategoryNameBinding
import com.kapilguru.student.databinding.StudyMaterialRecyclerBinding
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import com.kapilguru.student.myClassRoomDetails.model.StudyMaterialResponseApi

class  StudyMaterialAdapter(val listener: StudyMaterialListener) : RecyclerView.Adapter<StudyMaterialAdapter.ViewHolder>() {
    private val TAG = "StudyMaterialAdapter"
    private var mTopCategoriesList: List<StudyMaterialResponseApi> = ArrayList()

    fun setData(studyMaterialResponseApi: List<StudyMaterialResponseApi>) {
        mTopCategoriesList = studyMaterialResponseApi
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: StudyMaterialRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onTap(mTopCategoriesList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudyMaterialRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mTopCategoriesList[position]
    }

    override fun getItemCount(): Int {
        return mTopCategoriesList.size
    }

    interface StudyMaterialListener {
        fun onTap(studyMaterialResponseApi: StudyMaterialResponseApi)
    }
}