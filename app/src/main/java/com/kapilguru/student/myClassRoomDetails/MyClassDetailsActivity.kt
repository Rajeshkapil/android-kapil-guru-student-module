package com.kapilguru.student.myClassRoomDetails

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.PARAM_BATCH_ID
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityMyClassDetailsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.myClassRoomDetails.viewModel.MyClassDetailsFactory
import com.kapilguru.student.myClassRoomDetails.viewModel.MyClassDetailsViewModel

class MyClassDetailsActivity : BaseActivity() {

    lateinit var binding: ActivityMyClassDetailsBinding
    private lateinit var viewModel: MyClassDetailsViewModel
    lateinit var pageAdapter: MyClassroomDetailsFragmentAdapter
    private val TAG = "MyClassDetails"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_class_details)
        viewModel = ViewModelProvider(this, MyClassDetailsFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE),application))
            .get(MyClassDetailsViewModel::class.java)
        binding.lifecycleOwner = this
        setActionbarBackListener(this, binding.customActionBar.root, getString(R.string.title_myclassroom))
        getIntentData()
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        setTabLayoutPosition()
        registerOnPageChangeCallBacks()
    }

    private fun getIntentData() {
        viewModel.batchId.value = intent.getStringExtra(PARAM_BATCH_ID)
    }

    private fun setFragmentAdapter() {
        pageAdapter = MyClassroomDetailsFragmentAdapter(supportFragmentManager, lifecycle)
        pageAdapter.setBatchId(viewModel.batchId.value)
        binding.viewPager.adapter = pageAdapter
    }

    private fun addTabs() {
        for (i in 0 until pageAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(pageAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 0) {
                binding.tabLayout.selectTab(tab)
            }
        }
    }

    private fun setTabLayoutPosition(){
        val position = intent.getIntExtra("position",0)
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
        tabBackGroundColors(binding.tabLayout.getTabAt(position),true)
        binding.viewPager.currentItem = position
    }

    private fun addTabSelectListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab, true)
                binding.viewPager.currentItem = tab!!.position
            }
        })
    }


    private fun registerOnPageChangeCallBacks() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        view?.let {
            if (selected) {
                varView!!.background.setTint(ContextCompat.getColor(view.context, R.color.blue_3))
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
            } else {
                varView!!.background.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue_3))
            }
        }
    }


    companion object {
        val batch_data = "BATCH_DATA_KEY"

        fun launchActivity(batchId: String?, activity: Activity, viewPagerPosition: Int) {
            val intent = Intent(activity, MyClassDetailsActivity::class.java)
            intent.putExtra(PARAM_BATCH_ID,batchId)
            intent.putExtra("position",viewPagerPosition)
            activity.startActivity(intent)
        }
    }

}