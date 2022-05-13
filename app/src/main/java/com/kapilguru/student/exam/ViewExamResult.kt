package com.kapilguru.student.exam

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityViewExamResultBinding
import com.kapilguru.student.exam.model.QuestionsRequest
import com.kapilguru.student.exam.model.StudentReportRequest
import com.kapilguru.student.network.RetrofitNetwork

class ViewExamResult : BaseActivity() {
    val TAG = "ViewExamResult"
    lateinit var fragmentAdapter: ExamFragmentAdapter
    lateinit var binding : ActivityViewExamResultBinding
    lateinit var viewModel : ExamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setFragmentAdapters()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_exam_result)
        viewModel = ViewModelProvider(this, ExamViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application)).get(ExamViewModel::class.java)
        binding.lifecycleOwner = this
    }

    private fun setFragmentAdapters() {
        val studentReportRequest: StudentReportRequest?= getIntentData()
        val questionsRequest: QuestionsRequest?= getQuestionsIntentData()
        val fragmentManager = supportFragmentManager
        fragmentAdapter = ExamFragmentAdapter(fragmentManager, lifecycle,studentReportRequest,questionsRequest)
        binding.viewPager.adapter = fragmentAdapter
        setActionbarBackListener(this, binding.customActionBar.root, "Exams")
        addTabsAndSetDefaultTab()
        setTabLayoutPosition()
        clickListeners()
    }

    private fun getIntentData() : StudentReportRequest? = intent?.getParcelableExtra<StudentReportRequest>(PARAM_REPORTS_REQUEST)

    private fun getQuestionsIntentData() : QuestionsRequest? = intent?.getParcelableExtra<QuestionsRequest>(PARAM_QUESTIONS_REQUEST)


    private fun addTabsAndSetDefaultTab() {
        for (i in 0 until fragmentAdapter.itemCount) {
            val tab = binding.tabLayout.newTab().setCustomView(fragmentAdapter.setCustomTabView(i))
            binding.tabLayout.addTab(tab)
            if (i == 1) {
                binding.tabLayout.selectTab(tab)
            }
        }
    }

    private fun setTabLayoutPosition() {
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
        tabBackGroundColors(binding.tabLayout.getTabAt(0),true)
        binding.viewPager.currentItem = 0
    }

    private fun clickListeners() {
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

    private fun tabBackGroundColors(tab: TabLayout.Tab?, selected: Boolean) {
        val view = tab?.customView
        val varView = view?.findViewById<CardView>(R.id.cardView)
        val headerTitle = view?.findViewById<TextView>(R.id.tv_title)
        view?.let {
            if (selected) {
                varView!!.background.setTint(Color.BLUE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.white))

            } else {
                varView!!.background.setTint(Color.WHITE)
                headerTitle!!.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
            }
        }
    }
}