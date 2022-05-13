package com.kapilguru.student.referandearn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentReferAndEarnBinding
import com.kapilguru.student.showSingleButtonErrorDialog
import com.kapilguru.student.referandearn.myReferrals.MyReferralsListActivity

class ReferAndEarnFragment : Fragment() {
    lateinit var binding: FragmentReferAndEarnBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.actvMyReferrals.setOnClickListener {
            startActivity(Intent(requireContext(), MyReferralsListActivity::class.java))
        }
        binding.btnReferNow.setOnClickListener {
            onReferNowClicked()
        }
    }

    private fun onReferNowClicked() {
        if (canRefer()) {
            ReferAndEarnActivity.launchActivity(requireActivity())
        } else {
            showSingleButtonErrorDialog(requireContext(), "Looks like you are not subscribed,\n" + "Please Subscribe to Refer")
        }
    }

    /*Trainer can refer only if he is subscribed*/
    private fun canRefer(): Boolean {
        return true
    }
}