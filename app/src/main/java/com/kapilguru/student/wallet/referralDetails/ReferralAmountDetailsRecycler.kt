package com.kapilguru.student.wallet.referralDetails


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.AmountReferralDetailsRecyclerBinding
import com.kapilguru.student.wallet.model.EarningsReferralList

class ReferralAmountDetailsRecycler : RecyclerView.Adapter<ReferralAmountDetailsRecycler.Holder>() {

    var listOFEarningsDetailsReferrals = arrayListOf<EarningsReferralList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(val view: AmountReferralDetailsRecyclerBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = AmountReferralDetailsRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.model = listOFEarningsDetailsReferrals[position]
    }

    override fun getItemCount(): Int = listOFEarningsDetailsReferrals.size


}