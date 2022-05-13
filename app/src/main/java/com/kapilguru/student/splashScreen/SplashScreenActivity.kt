package com.kapilguru.student.splashScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.kapilguru.student.*
import com.kapilguru.student.homeActivity.HomeActivity
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.network.Status
import com.kapilguru.student.onBoarding.OnBoardingActivity
import com.kapilguru.student.preferences.StorePreferences

class SplashScreenActivity : AppCompatActivity() {
    private var appUpdateInfoTask: Task<AppUpdateInfo>? = null
    private val TAG = "SplashScreenActivity"
    lateinit var splashScreenViewModel: SplashScreenViewModel
    lateinit var dialog: CustomProgressDialog
    var appUpdateManager: AppUpdateManager? = null
    private val APP_UPDATE_REQUEST_CODE = 100
    private var listener: InstallStateUpdatedListener? = null
    var updatedToLatestVersion = true
    var alertDialog: AlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkUpdate()
        setUpViewModel()
//        verifyAccessToken()
//        viewModelObserver()
    }

    private fun checkUpdate() {
        Log.d(TAG, "checkUpdate: abc_1")
        initAppUpdateManager()
        appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        isUpdateAvailable()
    }

    private fun initAppUpdateManager() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        listener = InstallStateUpdatedListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                showSnackBarForCompleteUpdate()
                appUpdateManager!!.completeUpdate()
            } else {
                Log.d(TAG, "initAppUpdateManager: ${installState.installStatus()}")
            }
        }
        registerAppUpdateListener()
    }

    private fun registerAppUpdateListener() {
        Log.d(TAG, "checkUpdate: abc_4")
        listener?.let { appUpdateManager?.registerListener(it) }
    }

    private fun isUpdateAvailable() {
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                updatedToLatestVersion = false
                requestUpdate(appUpdateInfo)
            } else {
                unRegisterAppUpdateListener()
            }
        }
    }


    private fun requestUpdate(appUpdateInfo: AppUpdateInfo) {
        val check = appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, APP_UPDATE_REQUEST_CODE)
    }

    private fun unRegisterAppUpdateListener() {
        listener?.let { installStateUpdateListener ->
            appUpdateManager?.unregisterListener(installStateUpdateListener)
        }
        if (updatedToLatestVersion) {
            verifyAccessToken()
            viewModelObserver()
        }
    }

    private fun showSnackBarForCompleteUpdate() {
        showAppToast(this, "App Updated")
    }

    override fun onResume() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, APP_UPDATE_REQUEST_CODE)
            } else {
                isUpdateAvailable()
            }
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //  handle user's approval
                    updatedToLatestVersion = true
                    unRegisterAppUpdateListener()
                }
                Activity.RESULT_CANCELED -> {
                    updatedToLatestVersion = false
                    unRegisterAppUpdateListener()
                    checkUpdate()
                    //if you want to request the update again just call checkUpdate()
                    //  handle user's rejection
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    updatedToLatestVersion = false
                    //  handle update failure
                    unRegisterAppUpdateListener()
                    checkUpdate()
                }
                else -> {
                    updatedToLatestVersion = false
                    unRegisterAppUpdateListener()
                    checkUpdate()
                }
            }
        }
    }

    private fun setUpViewModel() {
        splashScreenViewModel = ViewModelProvider(
            this, SplashScreenViewModelFactory(
                ApiHelper(
                    RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE,
                )
            )
        ).get(SplashScreenViewModel::class.java)
        dialog = CustomProgressDialog(this)
    }

    private fun verifyAccessToken() {
        val pref = StorePreferences(this)
        if (pref.studentId != 0) {
            splashScreenViewModel.getProfileData(pref.studentId.toString())
        } else {
            navigateToInitialScreen()
        }
    }

    private fun navigateToInitialScreen() {
        val pref = StorePreferences(this)
        if (pref.trainerToken.isEmpty()) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun viewModelObserver() {
        splashScreenViewModel.profileDataResponse.observe(this, Observer { response ->
            Log.d(TAG, "viewModelObserver_16: ")
            when (response.status) {
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    Log.d(TAG, "viewModelObserver_2: ")
                    response.data?.let {
                        if (response.data.status == 200) {
                            navigateToInitialScreen()
                        } else {
                            clearSharedPreferences()
                        }
                    }
                    dialog.dismissLoadingDialog()
                }

                Status.ERROR -> {
                    Log.d(TAG, "viewModelObserver_3: ")
                    dialog.dismissLoadingDialog()
                    when (response.code) {
                        SESSION_OUT -> {
                            clearSharedPreferences()
                        }
                        NETWORK_CONNECTIVITY_EROR -> {
                            alertDialog?.isShowing?.let { isShowing ->
                                if(!isShowing) showErrorDialog(getString(R.string.network_connection_error))
                            }?: kotlin.run {
                                showErrorDialog(getString(R.string.network_connection_error))
                            }
                        }
                        else -> {
                            alertDialog?.isShowing?.let { isShowing ->
                                if(!isShowing) showErrorDialog(getString(R.string.try_again))
                            }
                        }
                    }
                }
            }
        })
    }

    private fun clearSharedPreferences() {
        val pref = StorePreferences(this)
        pref.clearStorePreferences()
        navigateToInitialScreen()
    }

    private fun showErrorDialog(message: String) {
         alertDialog  = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok) { dialog, id ->
                   dialog.dismiss()
                }
                setMessage(message)
            }
            builder.create()
        }
        alertDialog?.show()
    }
}