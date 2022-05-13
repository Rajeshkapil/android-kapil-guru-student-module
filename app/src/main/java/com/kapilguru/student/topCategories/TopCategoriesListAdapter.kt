package com.kapilguru.student.topCategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemTopCategoryNameBinding
import com.kapilguru.student.homeActivity.models.TopCategoriesApi

class  TopCategoriesListAdapter(val listener: TopCategoryClickListener) : RecyclerView.Adapter<TopCategoriesListAdapter.ViewHolder>() {
    private val TAG = "TopCategoriesListAdapter"
    private var mTopCategoriesList: ArrayList<TopCategoriesApi> = ArrayList()

    fun setData(topCategoriesList: ArrayList<TopCategoriesApi>) {
        mTopCategoriesList = topCategoriesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTopCategoryNameBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onTopCategoryClicked(mTopCategoriesList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopCategoryNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mTopCategoriesList[position]
    }

    override fun getItemCount(): Int {
        return mTopCategoriesList.size
    }

    interface TopCategoryClickListener {
        fun onTopCategoryClicked(topCategory: TopCategoriesApi)
    }
}