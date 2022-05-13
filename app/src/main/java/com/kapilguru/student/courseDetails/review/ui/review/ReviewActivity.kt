package com.kapilguru.student.courseDetails.review.ui.review

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.BaseActivity
import com.kapilguru.student.PARAM_IS_FROM
import com.kapilguru.student.R
import com.kapilguru.student.courseDetails.CourseDetailsModelFactory
import com.kapilguru.student.courseDetails.CourseDetailsViewModel
import com.kapilguru.student.databinding.ReviewActivityBinding
import com.kapilguru.student.network.RetrofitNetwork

class ReviewActivity : BaseActivity() {

    lateinit var fragmentAdapter: ReviewActivityAdapter
    lateinit var viewModel: CourseDetailsViewModel
    lateinit var binding: ReviewActivityBinding
    private var studentId: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.review_activity)
        viewModel = ViewModelProvider(this, CourseDetailsModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)))
            .get(CourseDetailsViewModel::class.java)

        binding.lifecycleOwner = this

        val fragmentManager = supportFragmentManager
        fragmentAdapter = ReviewActivityAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = fragmentAdapter
        viewModel.courseId.value = intent.getStringExtra("PARAM_COURSE_ID")
        setActionbarBackListener(this, binding.customActionBar.root, "Review")
        addTabsAndSetDefaultTab()
        setTabLayoutPosition(intent.getStringExtra(PARAM_IS_FROM))


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

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun addTabsAndSetDefaultTab() {
        for (i in 0 until fragmentAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(fragmentAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 1) {
                binding.tabLayout.selectTab(tab)
            }
        }
    }

    // if is from bell icon naviagate to 1st position else 0 tab
    private fun setTabLayoutPosition(isFromBellIcon: String?) {
        isFromBellIcon?.let {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            tabBackGroundColors(binding.tabLayout.getTabAt(1),true)
            binding.viewPager.currentItem = 1
        } ?:run{
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            tabBackGroundColors(binding.tabLayout.getTabAt(0),true)
            binding.viewPager.currentItem = 0
        }
    }

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        val tabIcon = view?.findViewById<ShapeableImageView>(R.id.img_icon)
        view?.let {
            if (selected) {
                varView!!.background.setTint(Color.BLUE)
                tabIcon!!.drawable.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))

            } else {
                varView!!.background.setTint(Color.WHITE)
                tabIcon!!.drawable.setTint(Color.BLUE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
            }
        }
    }
}