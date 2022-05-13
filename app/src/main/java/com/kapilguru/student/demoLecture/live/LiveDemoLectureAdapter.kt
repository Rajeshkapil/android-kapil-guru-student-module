package com.kapilguru.student.demoLecture.live

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.ItemLiveDemolectureBinding
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.webinar.model.OnGoingWebinar


class LiveDemoLectureAdapter(val mListener: LiveUpComingDemoLectureClickListener) : RecyclerView.Adapter<LiveDemoLectureAdapter.ViewHolder>() {
    private val TAG = "LiveUpComingClassAdapter"
    private var mLiveDemoLectureList = ArrayList<OnGoingDemoLectures>()

    fun setData(liveDemoLectureList: ArrayList<OnGoingDemoLectures>) {
        this.mLiveDemoLectureList = liveDemoLectureList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLiveDemolectureBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.acivShareIcon.setOnClickListener {
                mListener.shareClicked(mLiveDemoLectureList[bindingAdapterPosition])
            }

            binding.root.setOnClickListener {
                mListener.viewMoreClicked(mLiveDemoLectureList[bindingAdapterPosition])
            }
            binding.btnGoLive.setOnClickListener {
                mListener.onGoLiveClicked(mLiveDemoLectureList[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLiveDemolectureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mLiveDemoLectureList[position]

//        val hoursMinutesRead = datesDifferenceInMilli(mLiveUpComingClassList[position].startTime!!)
//        val totalDiff = hoursMinutesRead

//        object : CountDownTimer ((totalDiff) + 1000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                var seconds = (millisUntilFinished / 1000).toInt()
//                val hours = seconds / (60 * 60)
//                val tempMint = seconds - hours * 60 * 60
//                val minutes = tempMint / 60
//                seconds = tempMint - minutes * 60
//                holder.binding.timer.text = "TIME : " + String.format(
//                    "%02d",
//                    hours
//                ) + ":" + kotlin.String.format("%02d", minutes) + ":" + kotlin.String.format(
//                    "%02d",
//                    seconds
//                )
//            }
//            override fun onFinish() {
//                holder.binding.timer.text = "Live"
//            }
//        }.start()

    }

    override fun getItemCount(): Int {
        return mLiveDemoLectureList.size
    }

    interface LiveUpComingDemoLectureClickListener {
        fun shareClicked(liveDemoLecture: OnGoingDemoLectures)
        fun viewMoreClicked(liveDemoLecture: OnGoingDemoLectures)
        fun onGoLiveClicked(liveDemoLecture: OnGoingDemoLectures)
    }
}