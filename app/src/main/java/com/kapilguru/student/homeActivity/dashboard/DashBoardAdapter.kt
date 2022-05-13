package com.kapilguru.student.homeActivity.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.DashBoardItemViewBinding
import com.kapilguru.student.homeActivity.models.DashBoardItem

class DashBoardAdapter(var onItemClickedForHome: OnItemClickedForHome) :
    RecyclerView.Adapter<DashBoardAdapter.HomeViewHolder>() {

    var homeItems: ArrayList<DashBoardItem> = ArrayList()
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return homeItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = DashBoardItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.view.itemData = homeItems[position]
        holder.layoutView.setOnClickListener {
            onItemClickedForHome.onItemClick(position)
        }
    }

    class HomeViewHolder(itemView: DashBoardItemViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var layoutView = itemView.lLayoutHome
    }

    interface OnItemClickedForHome {
        fun onItemClick(position: Int)
    }
}