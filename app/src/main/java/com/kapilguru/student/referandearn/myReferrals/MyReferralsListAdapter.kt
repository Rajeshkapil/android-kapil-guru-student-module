package com.kapilguru.trainer.referandearn.myReferrals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kapilguru.student.databinding.MyReferralItemBinding
import com.kapilguru.student.referandearn.myReferrals.model.MyReferralResData

class MyReferralsListAdapter : RecyclerView.Adapter<MyReferralsListAdapter.ViewHolder>() {
    private var mMyReferralsList = ArrayList<MyReferralResData>()

    fun setData(myReferralsList: ArrayList<MyReferralResData>) {
        mMyReferralsList = myReferralsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: MyReferralItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyReferralItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = mMyReferralsList[position]
    }

    override fun getItemCount(): Int {
        return mMyReferralsList.size
    }
}