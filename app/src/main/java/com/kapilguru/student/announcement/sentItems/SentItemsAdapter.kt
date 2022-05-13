package com.kapilguru.student.announcement.sentItems

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.announcement.sentItems.data.SentItemsData
import com.kapilguru.student.databinding.SentItemsItemBinding

class SentItemsAdapter() :
    RecyclerView.Adapter<SentItemsAdapter.ItemViewHolder>() {
    private val TAG = "SentItemsAdapter"
    var sendItems = mutableListOf<SentItemsData>()
    var previousTappedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = SentItemsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.view.sendItem = sendItems[position]
        holder.view.position = position
        holder.view.handler = this
    }

    override fun getItemCount(): Int {
        return /*itemsList.size*/sendItems.size
    }

    fun setData(data: ArrayList<SentItemsData>) {
        sendItems = data
        notifyDataSetChanged()
    }

    fun dataVisibility(model: SentItemsData, tappedPosition: Int) {
        if(model.shouldShow == null){ // the value is not getting initilized in data class. So setting the value here.
            model.shouldShow = MutableLiveData(false)
        }
        if (tappedPosition == previousTappedPosition) {
            model.shouldShow.value = sendItems[tappedPosition].shouldShow.value != true
        } else {
            if (previousTappedPosition == -1) {
                previousTappedPosition = tappedPosition
            }
            sendItems[previousTappedPosition].shouldShow.value = false
            previousTappedPosition = tappedPosition
            model.shouldShow.value = true
        }
        notifyDataSetChanged()
    }


    class ItemViewHolder(binding: SentItemsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
    }
}