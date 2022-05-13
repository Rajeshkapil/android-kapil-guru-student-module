package com.kapilguru.student

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity() {

    private val TAG = "BaseActivity"
    var networkChangeObserver: MutableLiveData<Boolean> = MutableLiveData(true)
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var view: View

    override fun onStart() {
        super.onStart()
        networkCheck()
    }

    fun setActionbarBackListener(context: AppCompatActivity, view: View, title: String) {
        val activityName = view.findViewById<TextView>(R.id.actv_activity_name)
        val backIcon = view.findViewById<ImageView>(R.id.aciv_back)
        activityName.text = title
        backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun networkCheck() {
        connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(ConnectivityManager::class.java)
        } else ({
            this.getSystemService(Context.CONNECTIVITY_SERVICE)
        }) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
//                    networkChangeObserver.postValue(true)
                }

                override fun onLost(network: Network) {
                    if (this@BaseActivity::view.isInitialized) {
                        val snack = Snackbar.make(view.rootView, "Network connection lost", Snackbar.LENGTH_LONG)
                        val view = snack.view
                        view.setBackgroundColor(Color.parseColor("#d3d3d3"))
                        val txtView = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                        txtView.setTextColor(Color.parseColor("#000000"))

                        snack.show()
                        networkChangeObserver.postValue(false)
                    }

                }
            })
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }

    fun showSessionOutDialog() {
        val fm: FragmentManager = supportFragmentManager
        val sessionLogOutDialogFragment = SessionLogOutDialogFragment()
        sessionLogOutDialogFragment.show(fm, "session_logout_dialog")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}

