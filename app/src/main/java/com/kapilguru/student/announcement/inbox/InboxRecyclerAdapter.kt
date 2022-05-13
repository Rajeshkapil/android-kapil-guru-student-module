package com.kapilguru.student.announcement.inbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.announcement.inbox.data.InboxItem
import com.kapilguru.student.databinding.InboxItemBinding

class InboxRecyclerAdapter(val inboxListTOAdapters: InboxListTOAdapters) :
    RecyclerView.Adapter<InboxRecyclerAdapter.Holder>() {
    val TAG = "InboxRecyclerAdapter"

    var inboxItems = mutableListOf<InboxItem>()
    var previousTappedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = InboxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.model = inboxItems[position]
        holder.view.position = position
        holder.view.handler = this
    }

    override fun getItemCount(): Int {
        return inboxItems.size
    }

    fun setData(data: ArrayList<InboxItem>) {
        inboxItems = data
        notifyDataSetChanged()
    }

    class Holder(itemView: InboxItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val view = itemView
    }

    fun dataVisibility(model: InboxItem, tappedPosition: Int) {
        if (tappedPosition == previousTappedPosition) {
            model.shouldShow.value = inboxItems[tappedPosition].shouldShow.value != true
//            notifyItemChanged(tappedPosition)
        } else {
            if (previousTappedPosition == -1) {
                previousTappedPosition = tappedPosition
            }
            inboxItems[previousTappedPosition].shouldShow.value = false
//            notifyItemChanged(previousTappedPosition)
            previousTappedPosition = tappedPosition
            model.shouldShow.value = true
//            notifyItemChanged(tappedPosition)
        }
        notifyDataSetChanged()
    }

}

interface InboxListTOAdapters {
    fun getInboxList(inboxItem: InboxItem)
}