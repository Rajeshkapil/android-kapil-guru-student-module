package com.kapilguru.student

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.MutableLiveData


class AppConnectivityCheck(var context: Context) {
    private var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getConnectivityStatus() : Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val capabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                result = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        2
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        1
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        3
                    }
                    else -> {
                        4
                    }
                }
            }else{
                result =4
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            result = if (activeNetwork != null) {
                // connected to the internet
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        2
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        1
                    }
                    ConnectivityManager.TYPE_VPN -> {
                        3
                    }
                    else -> {
                        4
                    }
                }
            } else {
                4
            }
        }

        return result
    }


}