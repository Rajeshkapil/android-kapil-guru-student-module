package com.kapilguru.student.searchCourse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.SearchCourseItemBinding
import com.kapilguru.student.fromBase64
import com.kapilguru.student.searchCourse.model.PositionArrayItem
import kotlinx.android.synthetic.main.countryname_spinner_item.view.*
import kotlinx.android.synthetic.main.custom_key_value_view.view.*

class SearchAdapter(var onItemClickedForHome: OnItemClick) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    var item: ArrayList<PositionArrayItem> = ArrayList()
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchCourseItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = item[position]
        data.let {
            holder.view.model = it

            holder.view.batchType.text_value.text = it.batchtype
            holder.view.language.text_value.text = it.language
        }


    }

   inner class ViewHolder(itemView: SearchCourseItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var view = itemView
        var card = view.card
       var button = view.knowMore
        init {
            card.setOnClickListener {
                onItemClickedForHome.onItemClick(item[absoluteAdapterPosition])
            }
            button.setOnClickListener {
                onItemClickedForHome.onItemClick(item[absoluteAdapterPosition])
            }
        }

    }

    interface OnItemClick {
        fun onItemClick(item: PositionArrayItem)
    }
}