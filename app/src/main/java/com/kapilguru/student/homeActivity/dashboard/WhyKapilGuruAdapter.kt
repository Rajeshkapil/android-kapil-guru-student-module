package com.kapilguru.student.homeActivity.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.DashBoardItemViewBinding
import com.kapilguru.student.databinding.WhyKapilGuruItemViewBinding
import com.kapilguru.student.homeActivity.models.DashBoardItem

class WhyKapilGuruAdapter :
    RecyclerView.Adapter<WhyKapilGuruAdapter.HomeViewHolder>() {

    var homeItems: ArrayList<DashBoardItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = homeItems.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = WhyKapilGuruItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.view.itemData = homeItems[position]
//        holder.layoutView.setOnClickListener {
//            onItemClickedForHome.onItemClick(position)
//        }
    }

    class HomeViewHolder(itemView: WhyKapilGuruItemViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
//        var layoutView = itemView.lLayoutHome
    }

}