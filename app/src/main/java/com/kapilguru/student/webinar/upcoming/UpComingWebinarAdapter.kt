package com.kapilguru.student.webinar.upcoming

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemUpcomingWebinarBinding
import com.kapilguru.student.datesDifferenceInMilli
import com.kapilguru.student.webinar.live.LiveWebinarAdapter
import com.kapilguru.student.webinar.model.OnGoingWebinar

class UpComingWebinarAdapter(val listener: UpComingWebinarClickListener) : RecyclerView.Adapter<UpComingWebinarAdapter.ViewHolder>() {
    private val TAG = "UpComingWebinarAdapter"
    private var mUpComingWebinarList = ArrayList<OnGoingWebinar>()
    private val mListener = listener

    fun setData(onGoingWebinarList: ArrayList<OnGoingWebinar>) {
        this.mUpComingWebinarList = onGoingWebinarList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemUpcomingWebinarBinding) : RecyclerView.ViewHolder(binding.root) {
        var timer: CountDownTimer? = null

        init {
            binding.acivShareIcon.setOnClickListener {
                mListener.shareClicked(mUpComingWebinarList[bindingAdapterPosition])
            }

            binding.root.setOnClickListener {
                mListener.viewMoreClicked(mUpComingWebinarList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingWebinarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mUpComingWebinarList[position]
        setTimerText(holder,position)
    }

    private fun setTimerText(holder: ViewHolder, position: Int){
        if (holder.timer != null)
            holder.timer!!.cancel()

        val hoursMinutesRead = datesDifferenceInMilli(mUpComingWebinarList[position].startDate!!)
        val totalDiff = hoursMinutesRead
        holder.timer =  object : CountDownTimer((totalDiff) + 1000 , 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val hours = seconds / (60 * 60)
                val tempMint = seconds - hours * 60 * 60
                val minutes = tempMint / 60
                seconds = tempMint - minutes * 60
                holder.binding.timer.text = "TIME : " + String.format(
                    "%02d",
                    hours
                ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
                    "%02d",
                    seconds
                )
            }
            override fun onFinish() {
                holder.binding.timer.text = "Live"
            }
        }.start()
    }

    override fun getItemCount(): Int {
        return mUpComingWebinarList.size
    }

    interface UpComingWebinarClickListener {
        fun shareClicked(upComingClass: OnGoingWebinar)
        fun viewMoreClicked(upComingClass: OnGoingWebinar)
    }
}