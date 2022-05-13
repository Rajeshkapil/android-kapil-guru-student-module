package com.kapilguru.student.myClassroom


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityMyClassroomBinding
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.myClassroom.viewModel.MyClassroomViewModel
import com.kapilguru.student.myClassroom.viewModel.MyClassroomViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork


class MyClassroomFragment : Fragment() {

    lateinit var binding: ActivityMyClassroomBinding
    lateinit var viewModel: MyClassroomViewModel
    lateinit var pageAdapter: MyClassroomFragmentAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = ActivityMyClassroomBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, MyClassroomViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(MyClassroomViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shouldShowActionBarSearch(true)
        ((activity) as HomeActivity).fetchLatestNotification()
    }

    private fun shouldShowActionBarSearch(shouldShowActionSearchBar: Boolean){
        ((activity) as HomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }

    private fun setFragmentAdapter() {
        pageAdapter = MyClassroomFragmentAdapter(childFragmentManager, lifecycle)
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

            private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
                val view = tab?.customView
                val varView = view?.findViewById<CardView>(R.id.cardView)
                val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
                val subTitle = view?.findViewById<TextView>(R.id.tv_sub_title)
                view?.let {
                    if (selected) {
                        varView!!.background.setTint(Color.BLUE)
                        headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                        subTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                    } else {
                        varView!!.background.setTint(Color.WHITE)
                        headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                        subTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.black_2))
                    }
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun upDateTime() {

    }

}