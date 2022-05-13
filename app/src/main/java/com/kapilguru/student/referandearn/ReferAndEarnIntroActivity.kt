package com.kapilguru.student.referandearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityReferAndEarnIntroBinding
import com.kapilguru.student.referandearn.myReferrals.MyReferralsListActivity
import com.kapilguru.student.showSingleButtonErrorDialog

class ReferAndEarnIntroActivity : AppCompatActivity() {
    private val TAG = "ReferAndEarnIntroActivity"
    lateinit var binding: ActivityReferAndEarnIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn_intro)
        setActivityName()
        setClickListeners()
    }

    private fun setActivityName(){
        binding.customActionBar.actvActivityName.text = getString(R.string.refer_and_earn)
    }

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
        binding.actvMyReferrals.setOnClickListener {
            startActivity(Intent(this, MyReferralsListActivity::class.java))
        }
        binding.btnReferNow.setOnClickListener {
            onReferNowClicked()
        }
    }

    private fun onReferNowClicked() {
            ReferAndEarnActivity.launchActivity(this)
    }

    companion object {
        fun launchActivity(activity: Activity) {
            val intent = Intent(activity, ReferAndEarnIntroActivity::class.java)
            activity.startActivity(intent)
        }
    }
}