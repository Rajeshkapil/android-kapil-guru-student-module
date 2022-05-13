package com.kapilguru.student.demoLecture

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.MyApplication
import com.kapilguru.student.R
import com.kapilguru.student.databinding.ActivityWebinarBinding
import com.kapilguru.student.demoLecture.model.ActiveDemoLectures
import com.kapilguru.student.demoLecture.model.OnGoingDemoLectures
import com.kapilguru.student.demoLecture.viewModel.DemoLectureViewModel
import com.kapilguru.student.demoLecture.viewModel.DemoLectureViewModelFactory
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.model.LanguageData
import org.json.JSONArray

class DemoLectureActivity : AppCompatActivity() {
    private val TAG = "DemoLectureActivity"

    lateinit var binding: ActivityWebinarBinding
    lateinit var viewModel: DemoLectureViewModel
    lateinit var pageAdapter: DemoLecturePageAdapter
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar)
        viewModel = ViewModelProvider(this, DemoLectureViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(DemoLectureViewModel::class.java)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setActivityName()
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()
        viewModelObservers()
        setClickListeners()
    }

    /*As we are using same layout for webinar and demo lectures, we are setting the activity name here*/
    private fun setActivityName() {
        binding.customActionBar.actvActivityName.text = getString(R.string.demo_lectures)
    }

    private fun setFragmentAdapter() {
        val fragmentManager = supportFragmentManager
        pageAdapter = DemoLecturePageAdapter(fragmentManager, lifecycle, this)
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
        })
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

    private fun viewModelObservers() {
        viewModel.getAllWebinars()
        viewModel.demoLectureResponseAPIMutable.observe(this, Observer {
            when (it.status) {
                 Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    val onGoingDemoLectures = it.data?.demoLectureResponse?.ongoing
                    onGoingDemoLectures?.let { onGoingDemoLecturesString ->
                        val jsonArray = JSONArray(onGoingDemoLecturesString)
                        val onGoingList = ArrayList<OnGoingDemoLectures>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val liveUpComingClassData = OnGoingDemoLectures().toJSONObject(jsonObject)
                            onGoingList.add(liveUpComingClassData)
                        }
                        viewModel.liveOnGoingDemoLectures.value = onGoingList
                    }

                    val activeDemoLectures = it.data?.demoLectureResponse?.active
                    activeDemoLectures?.let { activeDemoLecturesString ->
                        val activeJsonArray = JSONArray(activeDemoLecturesString)
                        val activeList = ArrayList<ActiveDemoLectures>()
                        (application as MyApplication).getLanguages(object : MyApplication.FetchLanguagesListener {
                            override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
                                for (i in 0 until activeJsonArray.length()) {
                                    val jsonObject = activeJsonArray.getJSONObject(i)
                                    val activeDemoLecture = ActiveDemoLectures().toJSONObject(jsonObject, languagesDataList)
                                    activeList.add(activeDemoLecture)
                                }
                            }
                        })
                        viewModel.activeDemoLectures.value = activeList
                    }
                }
                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
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

    private fun setClickListeners() {
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }
}