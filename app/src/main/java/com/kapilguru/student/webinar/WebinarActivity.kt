package com.kapilguru.student.webinar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.webinar.model.ActiveWebinar
import com.kapilguru.student.webinar.model.LanguageData
import com.kapilguru.student.webinar.model.OnGoingWebinar
import com.kapilguru.student.webinar.viewModel.WebinarViewModel
import com.kapilguru.student.webinar.viewModel.WebinarViewModelFactory
import org.json.JSONArray

class WebinarActivity : AppCompatActivity() {

    lateinit var binding: ActivityWebinarBinding
    lateinit var viewModel: WebinarViewModel
    lateinit var pageAdapter: WebinarPageAdapter
    private val TAG = "WebinarActivity"
    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webinar)
        viewModel = ViewModelProvider(this, WebinarViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(WebinarViewModel::class.java)
        binding.lifecycleOwner = this
        dialog = CustomProgressDialog(this)
        setFragmentAdapter()
        addTabSelectListener()
        addTabs()
        registerOnPageChangeCallBacks()
        viewModelObservers()
        setClickListeners()
    }

    private fun setFragmentAdapter() {
        val fragmentManager = supportFragmentManager
        pageAdapter = WebinarPageAdapter(fragmentManager, lifecycle)
        binding.viewPager.adapter = pageAdapter
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

    private fun addTabs() {
        for (i in 0 until pageAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(pageAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 0) {
                binding.tabLayout.selectTab(tab)
            }
        }
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
        viewModel.webinarResponseAPIMutable.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    val onGoing = it.data?.webinarResponse?.ongoing
                    onGoing?.let {
                        val onGoingJsonArray = JSONArray(onGoing.toString())
                        val onGoingList = ArrayList<OnGoingWebinar>()
                        for (i in 0 until onGoingJsonArray.length()) {
                            val jsonObject = onGoingJsonArray.getJSONObject(i)
                            val liveUpComingWebinar = OnGoingWebinar().toJSONObject(jsonObject)
                            onGoingList.add(liveUpComingWebinar)
                        }
                        viewModel.liveUpComingClasses.value = onGoingList
                    }

                    val activeResponse = it.data?.webinarResponse?.active
                    activeResponse?.let {
                        val activeJsonArray = JSONArray(activeResponse.toString())
                        val activeList = ArrayList<ActiveWebinar>()
                        (application as MyApplication).getLanguages(object  : MyApplication.FetchLanguagesListener{
                            override fun onLanguagesFetched(languagesDataList: ArrayList<LanguageData>) {
                                for (i in 0 until activeJsonArray.length()) {
                                    val jsonObject = activeJsonArray.getJSONObject(i)
                                    val activeWebinar = ActiveWebinar().toJSONObject(jsonObject,languagesDataList)
                                    activeList.add(activeWebinar)
                                }
                            }
                        })
                        viewModel.activeWebinarList.value = activeList
                        Log.d(TAG, "viewModelObservers: $activeList")
                    }
                    dialog.dismissLoadingDialog()
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

    private fun setClickListeners(){
        binding.customActionBar.acivBack.setOnClickListener {
            finish()
        }
    }
}