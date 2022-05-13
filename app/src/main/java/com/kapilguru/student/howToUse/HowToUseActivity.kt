package com.kapilguru.student.howToUse

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityHowToUseBinding

class HowToUseActivity : AppCompatActivity() {
    private val TAG = "HowToUseActivity"
    lateinit var binding : ActivityHowToUseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLateInitVariables()
        setActivityName()
        setClickListener()
        showEmptyView()
    }

    private fun initLateInitVariables(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_how_to_use)
    }

    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.title_howtouse)
    }


    private fun setClickListener(){
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }

    private fun showEmptyView() {
        binding.noDataAvailable.root.visibility = View.VISIBLE
        binding.noDataAvailable.tvNoData.text = getString(R.string.feature_coming_soon)
    }

    companion object{
        fun launchActivity(activity : Activity){
            val intent = Intent(activity,HowToUseActivity::class.java)
            activity.startActivity(intent)
        }
    }
}