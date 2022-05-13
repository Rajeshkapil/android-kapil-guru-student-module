package com.kapilguru.student.wallet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.kapilguru.student.R
import kotlinx.android.synthetic.main.earnings_merger_view.view.*

class EarningsMergerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val TAG = "EarningsMergerView"
    lateinit var itemClickListener: ItemClickListener
    var type: Int = 0

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    init {
        inflate(getContext(), R.layout.earnings_merger_view, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.earningsMergerView)
            val title = resources.getText(typedArray.getResourceId(R.styleable.earningsMergerView_earnings_title, R.string.available_amount))
            amount_type.text = title
            val titleValue = resources.getText(typedArray.getResourceId(R.styleable.earningsMergerView_earningsMergerView_earnings_amount, R.string.batch_time))
            amount.text = titleValue

            type = typedArray.getInteger(R.styleable.earningsMergerView_earnings_type,0)


            parentCard.setOnClickListener { _ ->
                if (footer_item.isVisible) {
                    footer_item.visibility = View.GONE
                } else {
                    footer_item.visibility = View.VISIBLE
                }
            }

            request_money.setOnClickListener {
                itemClickListener.onItemClick(AmountType.REQUESTAMOUNT)
            }


            course_view_more.setOnClickListener {
                if (isClickedTypeAvailable()) {
                    itemClickListener.onItemClick(AmountType.REFUNDAMOUNTAVAILABLE)
                } else {
//                    itemClickListener.onItemClick(AmountType.EXPECTEDCOURSEAMOUNT)
                }
            }

            webinar_view_more.setOnClickListener {
                if (isClickedTypeAvailable()) {
                    itemClickListener.onItemClick(AmountType.PRIZESAMOUNTAVAILABLE)
                } else {
//                    itemClickListener.onItemClick(AmountType.EXPECTEDWEBINARAMOUNT)
                }
            }

            referral_view_more.setOnClickListener {
                if (isClickedTypeAvailable()) {
                    itemClickListener.onItemClick(AmountType.REFERRALAMOUNTAVAILABLE)
                } else {
//                    itemClickListener.onItemClick(AmountType.EXPECTEDREFERRALAMOUNT)
                }
            }


        }


    }

    private fun isClickedTypeAvailable(): Boolean {
        return type == 0
    }


    interface ItemClickListener {
        fun onItemClick(amountType: AmountType)
    }
}

enum class AmountType {
    REFUNDAMOUNTAVAILABLE, REFERRALAMOUNTAVAILABLE, PRIZESAMOUNTAVAILABLE,REQUESTAMOUNT
}