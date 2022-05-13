package com.kapilguru.student.courseDetails.review.ui.review

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kapilguru.student.R
import com.kapilguru.student.announcement.sentItems.data.SentItemsData
import com.kapilguru.student.courseDetails.review.model.StudentReviewedData

import com.kapilguru.student.courseDetails.review.ui.review.placeholder.PlaceholderContent.PlaceholderItem
import com.kapilguru.student.databinding.FragmentReviewListBinding


class MyReviewListRecyclerViewAdapter() : RecyclerView.Adapter<MyReviewListRecyclerViewAdapter.ViewHolder>() {
    var reviewList: ArrayList<StudentReviewedData> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(FragmentReviewListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = reviewList[position]
        holder.view.data = item
    }

    override fun getItemCount(): Int = reviewList.size

    inner class ViewHolder(binding: FragmentReviewListBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding

        override fun toString(): String {
            return super.toString() +  "'"
        }
    }

    fun setData(data: ArrayList<StudentReviewedData>) {
        reviewList = data
        notifyDataSetChanged()
    }


}