package com.kapilguru.student.homeActivity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.kapilguru.student.*
import com.kapilguru.student.announcement.AnnouncementActivity
import com.kapilguru.student.certificate.CertificateListActivity
import com.kapilguru.student.completionRequest.CompletionRequestActivity
import com.kapilguru.student.homeActivity.dashboard.DashBoardFragment
import com.kapilguru.student.homeActivity.models.DataItem
import com.kapilguru.student.homeActivity.viewModel.HomeViewModel
import com.kapilguru.student.homeActivity.viewModel.HomeViewModelFactory
import com.kapilguru.student.howToUse.HowToUseActivity
import com.kapilguru.student.login.LoginActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.referandearn.ReferAndEarnIntroActivity
import com.kapilguru.student.searchCourse.SearchCourseActivity
import com.kapilguru.student.ui.changePassword.model.LogoutRequest
import com.kapilguru.student.ui.profile.ProfileOverViewActivity
import kotlinx.android.synthetic.main.dash_board_custom_action_bar.*
import kotlinx.android.synthetic.main.dash_board_custom_action_bar.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    lateinit var viewModel: HomeViewModel
    lateinit var progressDialog: CustomProgressDialog
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    private var doubleBackPressed = false
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        setActionBar()
        initVariables()
        viewModelObservers()
        setBottomNavigationView()
        setNavigationView()
        setClickListeners()
        readIntentData()
        createToast()
        setVersionName()
    }

    private fun setVersionName(){
        val versionName = BuildConfig.VERSION_NAME
        val actvVersionName = findViewById<AppCompatTextView>(R.id.actv_version_name)
        actvVersionName.text = versionName
    }

    private fun readIntentData() {
//        val shouldShowProfile = intent?.getBooleanExtra(showProfile, false)
//        shouldShowProfile?.let {
//            if(shouldShowProfile) {
//                intent?.putExtra(showProfile, true)
//                navController.navigate(R.id.navigation_profile)
//            }
//        }
    }

    private fun setBottomNavigationView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        drawerLayout = findViewById(R.id.container)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_myclassroom,
                R.id.navigation_profile, R.id.navigation_upcoming,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun enableOrDisableBottomNavigationMenuItems() {
        if (StorePreferences(this).isProfileUpdated == 1) {
            shouldEnableBottomNavMenuItems(true)
        } else {
            shouldEnableBottomNavMenuItems(false)
        }
    }

    /*
    * This function sets the menu click enable or disable functionality to menu items*/
    fun shouldEnableBottomNavMenuItems(enableBottomNavMenuItems: Boolean) {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        navView.menu.findItem(R.id.navigation_home).isEnabled = enableBottomNavMenuItems
//        navView.menu.findItem(R.id.navigation_myclassroom).isEnabled = enableBottomNavMenuItems
//        navView.menu.findItem(R.id.navigation_referandearn).isEnabled = enableBottomNavMenuItems
//        navView.menu.findItem(R.id.navigation_howtouse).isEnabled = enableBottomNavMenuItems
        if (!enableBottomNavMenuItems) {
//            navController.navigate(R.id.navigation_profile)
        }
    }

    private fun closeNavigationView() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            this.drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun setNavigationView() {
        val drawerLayout: NavigationView = findViewById(R.id.drawer_layout)
        drawerLayout.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    closeNavigationView()
                    ProfileOverViewActivity.launchActivity(this)
                    true
                }
                R.id.certificate -> {
                    closeNavigationView()
                    CertificateListActivity.launchActivity(this)
                    true
                }
                R.id.completion_request -> {
                    closeNavigationView()
                    CompletionRequestActivity.launchActivity(this)
                    true
                }
                R.id.refer_and_earn -> {
                    closeNavigationView()
                    ReferAndEarnIntroActivity.launchActivity(this)
                    true
                }
                R.id.how_to_use -> {
                    closeNavigationView()
                    HowToUseActivity.launchActivity(this)
                    true
                }
                R.id.get_in_touch -> {
                    closeNavigationView()
                    checkAndShowLetsGetInTouch()
                    true
                }
                R.id.log_out -> {
                    closeNavigationView()
                    val pref = StorePreferences(this)
                    viewModel.logoutUser(LogoutRequest(pref.studentId, pref.trainerToken))
                    true
                }
                else -> {
                    if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        this.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }
    }

    private fun setClickListeners() {
        val socialMediaAccounts: View = findViewById(R.id.social_media_accounts)
        val ivFaceBook: ImageView = socialMediaAccounts.findViewById(R.id.iv_facebook)
        val ivTwitter: ImageView = socialMediaAccounts.findViewById(R.id.iv_twitter)
        val ivInstagram: ImageView = socialMediaAccounts.findViewById(R.id.iv_instagram)
        val ivLinkedIn: ImageView = socialMediaAccounts.findViewById(R.id.iv_linked_in)
        val ivYoutube: ImageView = socialMediaAccounts.findViewById(R.id.iv_youtube)

        ivFaceBook.setOnClickListener {
            closeNavigationView()
            openUrl(this, FACEBOOK_URL)
        }
        ivTwitter.setOnClickListener {
            closeNavigationView()
            openUrl(this, TWITTER_URL)
        }
        ivInstagram.setOnClickListener {
            closeNavigationView()
            openUrl(this, INSTAGRAM_URL)
        }
        ivLinkedIn.setOnClickListener {
            closeNavigationView()
            openUrl(this, LINKED_IN_URL)
        }
        ivYoutube.setOnClickListener {
            closeNavigationView()
            openUrl(this, YOUTUBE_URL)
        }
    }

    private fun checkAndShowLetsGetInTouch() {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment
        navHostFragment?.let { navHostFragmentNotNull ->
            val fragment = navHostFragmentNotNull.childFragmentManager.fragments[0]
            if (fragment !is DashBoardFragment) {
                navigateToDashBoard(true)
            }
            scrollToLetsGetInTouch()
        }
    }

    private fun scrollToLetsGetInTouch() {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment
        navHostFragment?.let { navHostFragmentNotNull ->
            val fragment = navHostFragmentNotNull.childFragmentManager.fragments[0]
            if (fragment is DashBoardFragment) {
                fragment.scrollToLetsGetInTouch()
            }
        }
    }

    private fun setActionBar(shouldShowText:Boolean?=true) {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mCustomView: View = inflater.inflate(R.layout.dash_board_custom_action_bar, null)
        mCustomView.only_title.text = "Hi ${StorePreferences(this).userName.toUpperCase()}"
        if(shouldShowText == false) {
            mCustomView.only_title.visibility = View.VISIBLE
            mCustomView.search_course.visibility = View.GONE
        } else {
            mCustomView.only_title.visibility = View.GONE
            mCustomView.search_course.visibility = View.VISIBLE
        }
        mCustomView.menu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        mCustomView.aCImg_notification.setOnClickListener {
            navigateToMessages()
        }
        supportActionBar?.customView = mCustomView
        val toolbar: Toolbar = (mCustomView.parent) as Toolbar
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        supportActionBar!!.setCustomView(mCustomView, layoutParams)
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"))
        toolbar.setPadding(0, 0, 0, 0)
        toolbar.setContentInsetsAbsolute(0, 0)
        setActionBarListeners(mCustomView)
    }

    private fun setActionBarListeners(mCustomView: View) {
        mCustomView.search_course.setOnClickListener {
            startActivity(Intent(this, SearchCourseActivity::class.java))
        }

    }

    private fun navigateToMessages() {
        startActivity(Intent(this, AnnouncementActivity::class.java).putExtra(PARAM_IS_FROM, PARAM_IS_FROM_HOME_ACTIVITY))
    }

    private fun initVariables() {
        viewModel = ViewModelProvider(this, HomeViewModelFactory(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application))
            .get(HomeViewModel::class.java)
        progressDialog = CustomProgressDialog(this)
    }

    private fun viewModelObservers() {
        viewModel.logoutResponse.observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
//                    progressDialog.dismissLoadingDialog()
                    clearAllSharedPreferences()
                }
                Status.ERROR -> {
                    if(response.code == NETWORK_CONNECTIVITY_EROR) {
//                        showSingleButtonErrorDialog(this,getString(R.string.network_connection_error))
                    }
//                    progressDialog.dismissLoadingDialog()
                }
                Status.LOADING -> {
//                    progressDialog.showLoadingDialog()
                }
            }
        })

        viewModel.notificationCountResponse.observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let { it ->
                        it.notificationCountResponseApi?.let { list ->
                            if (list.isNotEmpty()) setNotificationCount(list[0])
                        }
                    }

                }
                Status.ERROR -> {
                    when (response.code) {
                        NETWORK_CONNECTIVITY_EROR -> {
                            Log.d(TAG, "viewModelObservers: ")
                        }
                    }
                }
                Status.LOADING -> {

                }
            }
        })

    }

    private fun clearAllSharedPreferences() {
        StorePreferences(this).clearStorePreferences()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun setNotificationCount(info: DataItem?) {
        info?.let {
            tv_Count.text = if (it.currCount!! - it.prevCount!! > 0) {
                (it.currCount!! - it.prevCount!!).toString()
            } else {
                ""
            }
        }
    }

    fun navigateToDashBoard(shouldScrollDown: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(DashBoardFragment.KEY_SHOULD_SCROLL_DOWN, shouldScrollDown)
        navController.navigate(R.id.navigation_home, bundle)
    }

    fun navigateToClassRooms() {
        navController.navigate(R.id.navigation_myclassroom)
    }

    fun navigateToUpcoming() {
        navController.navigate(R.id.navigation_upcoming)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {// use android.R.id
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        }

        if (item.itemId == R.id.log_out) {
            Log.v("cehcj_here", "yes")
        }

        return true
    }

    companion object {
        var showProfile = "showProfile"
    }

    fun fetchLatestNotification() {
        if(::viewModel.isInitialized) viewModel.notificationCountApiCall()
    }


    private fun createToast() {
        toast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        Log.d(TAG, "onBackPressed: ${navController.currentDestination?.navigatorName}")
        Log.d(TAG, "onBackPressed: ${navController.currentDestination?.label}")
        Log.d(TAG, "onBackPressed: ${navController.currentDestination?.id == R.id.navigation_home}")
        Log.d(TAG, "onBackPressed: ${navController.currentDestination?.parent}")

        when (navController.currentDestination?.id) {
            R.id.navigation_home -> {
                backDoublePressed()
            }
            R.id.navigation_profile -> {
                backDoublePressed()
            }
            R.id.navigation_myclassroom -> {
                backDoublePressed()
            }
            R.id.navigation_upcoming -> {
                backDoublePressed()
            }

            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun backDoublePressed() {
        if (doubleBackPressed) {
            toast?.cancel()
//            super.onBackPressed()
            finish()
            return
        }
        this.doubleBackPressed = true
        toast?.show()
        GlobalScope.launch {
            delay(1000)
            doubleBackPressed = false
        }
    }

    fun shouldShowSearchInActionBar(showSearch: Boolean) {
        setActionBar(showSearch)
    }


}