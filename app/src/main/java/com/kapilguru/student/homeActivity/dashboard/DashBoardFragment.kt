package com.kapilguru.student.homeActivity.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.kapilguru.student.*
import com.kapilguru.student.allExamsList.AllExamsListActivity
import com.kapilguru.student.allPopularTrendingCourses.AllPopularTrendingCourses
import com.kapilguru.student.allTrendingDemos.AllTrendingDemosActivity
import com.kapilguru.student.allTrendingWebinars.AllTrendingWebinars
import com.kapilguru.student.announcement.AnnouncementActivity
import com.kapilguru.student.certificate.CertificateListActivity
import com.kapilguru.student.databinding.FragmentDashBoardBinding
import com.kapilguru.student.demoLecture.DemoLectureActivity
import com.kapilguru.student.demoLecture.demoLectureDetails.DemoLectureDetailsActivity
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.homeActivity.dashboard.tabFragments.TrendingDemos
import com.kapilguru.student.homeActivity.dashboard.tabFragments.TrendingJobOpenings
import com.kapilguru.student.homeActivity.dashboard.tabFragments.TrendingWebinars
import com.kapilguru.student.homeActivity.models.CreateLeadRequest
import com.kapilguru.student.homeActivity.models.DashBoardItem
import com.kapilguru.student.homeActivity.models.TopCategoriesApi
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi
import com.kapilguru.student.homeActivity.popularAndTrending.PopularAndTrendingFragment
import com.kapilguru.student.jobOpenings.JobOpeningsActivity
import com.kapilguru.student.myClassRoomDetails.MyClassDetailsActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.searchCourse.SearchCourseActivity
import com.kapilguru.student.topCategories.TopCategoriesListActivity
import com.kapilguru.student.topCategories.selectedTopCategory.SelectedTopCategoryActivity
import com.kapilguru.student.wallet.EarningsActivity
import com.kapilguru.student.webinar.WebinarActivity
import com.kapilguru.student.webinar.webinarDetails.WebinarDetailsActivity
import kotlinx.android.synthetic.main.activity_apply_job_openings.view.*
import kotlinx.android.synthetic.main.contact_details_layout.view.*
import kotlinx.android.synthetic.main.dash_board_custom_action_bar.view.*
import kotlinx.android.synthetic.main.dash_board_tab_ui.*
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_dash_board.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class DashBoardFragment : Fragment(), DashBoardAdapter.OnItemClickedForHome, TodayScheduleAdapter.OnItemClick, TodayCategoriesAdapter.TopCategoriesCardClick, CardClickListener {

    lateinit var homeViewBinding: FragmentDashBoardBinding
    lateinit var homeAdapter: DashBoardAdapter
    lateinit var homeViewPagerAdapter: DashBoardViewPagerAdapter
    lateinit var todayScheduleAdapter: TodayScheduleAdapter
    lateinit var topCategoriesAdapter: TodayCategoriesAdapter
    lateinit var whyKapilGuruAdapter: WhyKapilGuruAdapter
    lateinit var homeScreenViewModel: DashBoardViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var dialogFragmentCustom: DialogFragmentCustom
    var currentTabIndex: Int = 0
    var topHomePagerposition: Int = 0
    private val TAG = "DashBoardFragment"
    private var isPermisionAccepted: MutableLiveData<Boolean?> = MutableLiveData()
    val timer = Timer()

    companion object {
        const val KEY_SHOULD_SCROLL_DOWN = "SHOULD_SCROLL_DOWN"
        fun newInstance() = DashBoardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)
        homeScreenViewModel = ViewModelProvider(this, DashBoardViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), requireActivity().application))
            .get(DashBoardViewModel::class.java)
        homeViewBinding.viewModel = homeScreenViewModel
        homeViewBinding.lifecycleOwner = this
        progressDialog = CustomProgressDialog(requireActivity())
        dialogFragmentCustom = DialogFragmentCustom()
//        showDialog()
//        setTodaySchedule()
        setTopCategories()
        setPopularCourses()

        return homeViewBinding.root
    }

    private fun setGetInTouch() {
        homeViewBinding.contactLayout.phoneNumber.setOnClickListener {
            checkPhonePermission()
        }

        homeViewBinding.send.setOnClickListener {
            if (homeScreenViewModel.validateCreateLead()) {
                homeScreenViewModel.createLeadApi()
            }
        }

        homeScreenViewModel.createLeadError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
        })

        homeScreenViewModel.commonResponse.observe(viewLifecycleOwner, Observer { it ->
            // Clear all the fields
            homeScreenViewModel.createLeadRequest.value = CreateLeadRequest()

            when (it.status) {
                Status.ERROR -> {
                    showToast("Something Went Wrong Please, Try After Some Time")
                }
                Status.SUCCESS -> {
                    showToast("Thanks For approaching us, Our concern team will Contact you")
                }
                Status.LOADING -> {
//                    Do Nothing
                }
            }
        })

        isPermisionAccepted.observe(viewLifecycleOwner, Observer { it ->
            it?.let { isAccepted ->
                if (isAccepted) {
                    contactPhoneIntent(this.requireContext(), getString(R.string.contactNumber))
                }
            }
        })

        homeViewBinding.contactLayout.contactMail.setOnClickListener {
            contactEmailIntent(this.requireContext(), getString(R.string.info_mail))
        }
    }

    private fun showToast(message: String) {
        val toast = Toast(activity)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.setText(message)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    private fun setPopularCourses() {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.popularCourses, PopularAndTrendingFragment.newInstance())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setClickListeners() {
        btn_trending_view_all.setOnClickListener {
            when (currentTabIndex) {
                0 -> navigateToAllWebinars()
                1 -> navigateToAllDemos()
                2 -> navigateToAllJobOpenings()
            }
        }
        homeViewBinding.tabsViewAll.setOnClickListener {
            TopCategoriesListActivity.launchActivity(requireActivity())
        }

        homeViewBinding.searchCourse.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchCourseActivity::class.java))
        }

    }

    private fun checkPhonePermission() {
        contactPhone(this.requireContext(), isPermisionAccepted)
    }

    private fun navigateToAllJobOpenings() {
        startActivity(Intent(requireContext(), JobOpeningsActivity::class.java))
    }

    private fun navigateToAllDemos() {
        startActivity(Intent(requireContext(), AllTrendingDemosActivity::class.java))
    }

    private fun navigateToAllWebinars() {
        startActivity(Intent(requireContext(), AllTrendingWebinars::class.java))
    }

    private fun setTopCategories() {
        homeViewBinding.recyclerTopCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        homeScreenViewModel.fetchTopCategories()
        topCategoriesAdapter = TodayCategoriesAdapter(this)
    }

    private fun setTodaySchedule() {
        homeScreenViewModel.fetchUpcomingSchedule()
        todayScheduleAdapter = TodayScheduleAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWhyKapilGuru()
        setGetInTouch()
        homeAdapter = DashBoardAdapter(this as DashBoardAdapter.OnItemClickedForHome)
        view.recyclerViewHome.adapter = homeAdapter
        viewModelObserver()
        viewPageAdapterSetup(view)
        setClickListeners()
        checkAndShowLetsGetInTouch()
        shouldShowActionBarSearch(false)
        ((activity) as HomeActivity).fetchLatestNotification()
    }

    private fun setWhyKapilGuru() {
        whyKapilGuruAdapter = WhyKapilGuruAdapter()
        homeScreenViewModel.setWhyKapilGuruItems()
        homeViewBinding.whyKapilRecycler.adapter = whyKapilGuruAdapter
    }

    private fun viewPageAdapterSetup(view: View) {
        homeViewPagerAdapter = DashBoardViewPagerAdapter(this)
        view.homeViewPager2.adapter = homeViewPagerAdapter
        viewPagerObserver()
        registerOnPageChangeCallBack()
        topSliderTimer()
    }

    private fun checkAndShowLetsGetInTouch() {
        var shouldScrollDown: Boolean? = false
        shouldScrollDown = arguments?.getBoolean(KEY_SHOULD_SCROLL_DOWN)
        if (shouldScrollDown != null && shouldScrollDown) {
            GlobalScope.launch {
                delay(500)
                scrollToLetsGetInTouch()
            }
        }
    }

    fun scrollToLetsGetInTouch() {
        homeViewBinding.scDashBoard.post(Runnable {
            homeViewBinding.scDashBoard.fullScroll(View.FOCUS_DOWN)
        })
    }

    private fun viewModelObserver() {
        homeScreenViewModel.setHomeItems()
        homeScreenViewModel.listOfHomeItems.observe(HomeScreenFragment@ this, Observer {
            homeAdapter.homeItems = it as ArrayList<DashBoardItem>
        })

        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@ this, Observer {
            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
            setOnboadingIndicator()
            // set default Indicator
            setCurrentOnboardingIndicators(0)
        })

        homeScreenViewModel.upcomingResponse.observe(HomeScreenFragment@ this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
//                    progressDialog.showLoadingDialog()
                    showDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { upComingSchedule ->
                        if (upComingSchedule.isNotEmpty()) {
                            homeViewBinding.horizontalRecycler.adapter = todayScheduleAdapter
                            todayScheduleAdapter.upComingScheduleApiList = upComingSchedule
                            (this.requireActivity().application as MyApplication).initMaintenanceWorker()
                            (this.requireActivity().application as MyApplication).getPendingIntent(upComingSchedule)
                        }
                    }
//                    progressDialog.dismissLoadingDialog()
                    dismissDialog()
                }

                Status.ERROR -> {
//                    progressDialog.dismissLoadingDialog()
                    dismissDialog()
                }
            }
        })

        homeScreenViewModel.topCategories.observe(HomeScreenFragment@ this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
//                    progressDialog.showLoadingDialog()
                    showDialog()
                }

                Status.SUCCESS -> {
                    response.data?.data?.let { topCategories ->
                        if (topCategories.isNotEmpty()) {
                            homeViewBinding.recyclerTopCategories.adapter = topCategoriesAdapter
                            topCategoriesAdapter.upComingScheduleApiList = topCategories.subList(0, 12)
                        }
                    }
//                    progressDialog.dismissLoadingDialog()
                    dismissDialog()
                }

                Status.ERROR -> {
//                    progressDialog.dismissLoadingDialog()
                    dismissDialog()
                    if(response.code == NETWORK_CONNECTIVITY_EROR) {
                        showSingleButtonErrorDialog(requireContext(),getString(R.string.network_connection_error))
                    }
                }
            }
        })

        homeScreenViewModel.setDashBoardTabsItems()
        homeScreenViewModel.listOfTabItems.observe(requireActivity(), Observer {
            setTabUI(it)
        })

        homeScreenViewModel.whyKapilGuruData.observe(requireActivity(), Observer {
            whyKapilGuruAdapter.homeItems = it as ArrayList<DashBoardItem>
        })
    }

    private fun setTabUI(dashBoardTabModel: MutableList<DashBoardCustomTabModel>) {
        // add tab Listeners
        addTabSelectListener()
        // add tabs UI
        for (i in 0 until dashBoardTabModel.size) {
            val tab = homeViewBinding.tabLayout.newTab().setCustomView(setCustomTabView(dashBoardTabModel[i]))
            homeViewBinding.tabLayout.addTab(tab)
            if (i == 0) {
                homeViewBinding.tabLayout.selectTab(tab)
            }
        }
        // Default Tab
        changeSelectedFragment(TrendingWebinars.newInstance())
    }

    fun setCustomTabView(dashBoardTabModel: DashBoardCustomTabModel): View {
        val view: View = LayoutInflater.from(this.requireActivity()).inflate(R.layout.dash_board_tab_ui, null)
        val header = view.findViewById<View>(R.id.tv_title) as TextView
        header.text = dashBoardTabModel.title
        val subTitle = view.findViewById<View>(R.id.tv_sub_title) as TextView
        subTitle.text = dashBoardTabModel.subTitle
        val imageView = view.findViewById<View>(R.id.side_icon) as ImageView
        imageView.setImageResource(dashBoardTabModel.image!!)
        return view
    }

    private fun addTabSelectListener() {
        homeViewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabBackGroundColors(tab)
                currentTabIndex = tab!!.position
                when (currentTabIndex) {
                    0 -> changeSelectedFragment(TrendingWebinars.newInstance())
                    1 -> changeSelectedFragment(TrendingDemos.newInstance())
                    2 -> changeSelectedFragment(TrendingJobOpenings.newInstance())
                }
            }

            private fun tabBackGroundColors(tab: TabLayout.Tab?) {
                val view = tab?.customView
                val varView = view?.findViewById<CardView>(R.id.cardView)
                view?.let {
                    if (tab.isSelected) {
                        varView!!.background.setTint(ContextCompat.getColor(view.context, R.color.gold))
                    } else {
                        varView!!.background.setTint(Color.WHITE)
                    }
                }
            }
        })
    }

    private fun changeSelectedFragment(fragment: Fragment) {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.tabFrameLayout, fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()
    }

    private fun setOnboadingIndicator() {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in 0 until homeViewPagerAdapter.itemCount) {
            val indicators = ImageView(this.requireContext())

            indicators.setImageDrawable(
                ContextCompat.getDrawable(
                    this.requireContext(), R.drawable.onboarding_indicator_active_two
                )
            )
            indicators.layoutParams = layoutParams
            homeViewBinding.layoutOnboardingIndicators.addView(indicators)
            homeViewBinding.layoutOnboardingIndicators.refreshDrawableState()
        }
    }

    private fun viewPagerObserver() {
        homeScreenViewModel.setHomeTopItems()
        homeScreenViewModel.listOfHomeTopItems.observe(HomeScreenFragment@ this, Observer {
            homeViewPagerAdapter.setViewPagerData(homeViewPagerItems = it)
        })
    }

    private fun registerOnPageChangeCallBack() {
        homeViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                topHomePagerposition = position
                setCurrentOnboardingIndicators(position)
            }
        })
    }

    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = homeViewBinding.layoutOnboardingIndicators.childCount
        for (i in 0..childCount) {
            val imageView = homeViewBinding.layoutOnboardingIndicators.getChildAt(i)?.let {
                it as ImageView
            }
            if (i == index) {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active))
            } else {
                imageView?.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.onboarding_indicator_active_two))
            }
        }
    }

    override fun onItemClick(position: Int) {
        when (position) {
            0 -> navigateToAllDemos()
            1 -> navigateToAllWebinars()
            2 -> onPopularTrendingClick()
            3 -> (activity as HomeActivity).navigateToClassRooms()
            4 -> (activity as HomeActivity).navigateToUpcoming()
            5 -> startActivity(Intent(activity, DemoLectureActivity::class.java))
            6 -> startActivity(Intent(activity, WebinarActivity::class.java))
            7 -> startActivity(Intent(activity, AllExamsListActivity::class.java))
            8 -> startActivity(Intent(activity, CertificateListActivity::class.java))
            9 -> navigateToAllJobOpenings()
            10 -> startActivity(Intent(activity, AnnouncementActivity::class.java))
            11 -> startActivity(Intent(activity, EarningsActivity::class.java))
        }
    }

    override fun onCardClick(upComingScheduleApi: UpComingScheduleApi) {
        when (upComingScheduleApi.activityType?.toLowerCase()) {

            "course" -> {
                MyClassDetailsActivity.launchActivity(upComingScheduleApi.activityId.toString(), requireActivity(), 0)
            }

            "webinar" -> {
                upComingScheduleApi.activityId?.let { id ->
                    WebinarDetailsActivity.launchActivity(requireActivity(), id)
                }
            }

            "lecture" -> {
                upComingScheduleApi.activityId?.let { id ->
                    DemoLectureDetailsActivity.launchActivity(requireActivity(), id)
                }
            }
        }
//        if(upComingScheduleApi.activityId)
    }

    override fun onTopCategoriesCardClick(cateogry: TopCategoriesApi) {
        SelectedTopCategoryActivity.launchActivity(cateogry, requireActivity())
    }

    fun showDialog() {
        activity?.let { it ->
            activity?.supportFragmentManager?.executePendingTransactions()
            val abc: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG)
            abc?.let {

            } ?: kotlin.run {
                val fm: FragmentManager = it.supportFragmentManager
                if (activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
                    dialogFragmentCustom.show(fm, DIALOG_FRAGMENT_TAG)
                }
            }

        }
    }

    fun dismissDialog() {
        val fm: Fragment? = activity?.supportFragmentManager?.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        fm?.let {
            val abc = fm as DialogFragmentCustom
            abc.dismiss()
        }

    }

    override fun onPopularTrendingClick() {
        startActivity(Intent(this.requireContext(), AllPopularTrendingCourses::class.java).apply {
            putParcelableArrayListExtra(PARAM_ALL_POPULAR_TRENDING_LIST, homeScreenViewModel.popularTrendingList)
        })
    }

    override fun onWebinarClick() {
        navigateToAllWebinars()
    }

    override fun onDemoLectureClick() {
        navigateToAllDemos()
    }

    private fun topSliderTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.d(TAG, "run_AMI_runing")
                homeViewBinding.homeViewPager2.post(Runnable {
                    homeViewBinding.homeViewPager2.homeViewPager2.currentItem = topHomePagerposition % 3
                    topHomePagerposition++
                })
            }
        }, 0, 5000)
    }



    private fun shouldShowActionBarSearch(shouldShowActionSearchBar: Boolean){
        ((activity) as HomeActivity).shouldShowSearchInActionBar(shouldShowActionSearchBar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog.dismissLoadingDialog()
        timer.cancel()
    }

}