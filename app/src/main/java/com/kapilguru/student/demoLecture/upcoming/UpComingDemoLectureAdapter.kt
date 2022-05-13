package com.kapilguru.student.demoLecture.upcoming

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemUpcomingDemoLectureBinding
import com.kapilguru.student.databinding.ItemUpcomingWebinarBinding
import com.kapilguru.student.datesDifferenceInMilli
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.webinar.upcoming.UpComingWebinarAdapter

class UpComingDemoLectureAdapter(val listener : UpComingDemoLectureClickListener) : RecyclerView.Adapter<UpComingDemoLectureAdapter.ViewHolder>() {
    private val TAG = "UpComingDemoLecture"
    private var mUpComingWebinarList = ArrayList<OnGoingDemoLectures>()
    private val mListener = listener

    fun setData(onGoingDemoLecturesList: ArrayList<OnGoingDemoLectures>) {
        this.mUpComingWebinarList = onGoingDemoLecturesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingDemoLectureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(val binding: ItemUpcomingDemoLectureBinding) : RecyclerView.ViewHolder(binding.root) {
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

    interface UpComingDemoLectureClickListener {
        fun shareClicked(upComingDemoLecture: OnGoingDemoLectures)
        fun viewMoreClicked(upComingDemoLecture: OnGoingDemoLectures)
    }
}