package com.kapilguru.student.onBoarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemContainerBoardingOneBinding


class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    private var onBoardingItems = mutableListOf<OnBoardingItem>()

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val view = ItemContainerBoardingOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.view.data = onBoardingItems[position]
    }

    fun setData(onBoardingItems: ArrayList<OnBoardingItem>) {
        this.onBoardingItems = onBoardingItems
        notifyDataSetChanged()
    }

    class OnBoardingViewHolder(itemView: ItemContainerBoardingOneBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val view = itemView
    }
}
