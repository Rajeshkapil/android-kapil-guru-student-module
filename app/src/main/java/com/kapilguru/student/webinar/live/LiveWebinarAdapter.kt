package com.kapilguru.student.webinar.live

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemLiveWebinarBinding
import com.kapilguru.student.webinar.model.OnGoingWebinar
import kotlin.collections.ArrayList

class LiveWebinarAdapter(val mListener: LiveWebinarClickListener) : RecyclerView.Adapter<LiveWebinarAdapter.ViewHolder>() {
    private val TAG = "LiveUpComingClassAdapter"
    private var onGoingList = ArrayList<OnGoingWebinar>()

    fun setData(onGoingWebinarList: ArrayList<OnGoingWebinar>) {
        this.onGoingList = onGoingWebinarList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLiveWebinarBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.acivShareIcon.setOnClickListener {
                    mListener.shareClicked(onGoingList[bindingAdapterPosition])
                }

                binding.root.setOnClickListener {
                    mListener.viewMoreClicked(onGoingList[bindingAdapterPosition])
                }
                binding.btnGoLive.setOnClickListener {
                    mListener.onGoLiveClicked(onGoingList[bindingAdapterPosition])
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLiveWebinarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = onGoingList[position]
    }

    override fun getItemCount(): Int {
        return onGoingList.size
    }

    interface LiveWebinarClickListener {
        fun shareClicked(liveWebinar: OnGoingWebinar)
        fun viewMoreClicked(liveWebinar: OnGoingWebinar)
        fun onGoLiveClicked(liveWebinar: OnGoingWebinar)
    }
}