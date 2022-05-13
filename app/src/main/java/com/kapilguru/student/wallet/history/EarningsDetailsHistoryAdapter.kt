package com.kapilguru.student.wallet.history


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.EarningsHistoryItemViewBinding
import com.kapilguru.student.wallet.history.model.EarningsHistoryResponse
import com.kapilguru.student.wallet.history.model.PaidBankDetails


class EarningsDetailsHistoryAdapter(val listener: EarningsDetailsHistoryAdapterListener, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<EarningsDetailsHistoryAdapter.Holder>() {

    var earningsHistoryResponse = listOf<EarningsHistoryResponse>()
    var previousTappedPosition: Int = -1
    var paidBankDetails: MutableLiveData<PaidBankDetails?> = MutableLiveData()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = EarningsHistoryItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.item.model = earningsHistoryResponse[position]
        holder.item.position = position
        holder.item.handler = this

    }

    override fun getItemCount(): Int = earningsHistoryResponse.size

    fun setData(earningsHistoryResponse: List<EarningsHistoryResponse>?) {
        earningsHistoryResponse?.let {
            this.earningsHistoryResponse = it
        }
        notifyDataSetChanged()
    }

    fun dataVisibility(model: EarningsHistoryResponse, tappedPosition: Int, item: EarningsHistoryItemViewBinding) {
        listener.getSelectedId(model.id.toString())
        onObservePaymentInfo(tappedPosition, model,item)
    }

    private fun onObservePaymentInfo(tappedPosition: Int, model: EarningsHistoryResponse, item: EarningsHistoryItemViewBinding) {
        paidBankDetails.observe(lifecycleOwner, Observer {
            item.paymentDetails = it
            if (tappedPosition == previousTappedPosition) {
                model.shouldShow.value = earningsHistoryResponse[tappedPosition].shouldShow.value != true
            } else {
                if (previousTappedPosition == -1) {
                    previousTappedPosition = tappedPosition
                }
                earningsHistoryResponse[previousTappedPosition].shouldShow.value = false
                previousTappedPosition = tappedPosition
                model.shouldShow.value = true
            }
            notifyDataSetChanged()
        })
    }

    inner class Holder(itemView: EarningsHistoryItemViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var item = itemView
        var studentDetailsCard = itemView.studentDetails
        var courseAmount = itemView.courseViewMore
        var referralAmount = itemView.referralViewMore
        var webinarAmount = itemView.webinarViewMore
        init {
            studentDetailsCard.setOnClickListener {
                dataVisibility(earningsHistoryResponse[absoluteAdapterPosition],absoluteAdapterPosition,item)
            }
            courseAmount.setOnClickListener {
                listener.onCourseAmountClick()
            }
            referralAmount.setOnClickListener {
                listener.onReferralAmountClick()
            }
            webinarAmount.setOnClickListener {
                listener.onWebinarAmountClick()
            }
        }

    }

    interface EarningsDetailsHistoryAdapterListener {
        fun getSelectedId(id: String)
        fun onCourseAmountClick()
        fun onReferralAmountClick()
        fun onWebinarAmountClick()
    }
}