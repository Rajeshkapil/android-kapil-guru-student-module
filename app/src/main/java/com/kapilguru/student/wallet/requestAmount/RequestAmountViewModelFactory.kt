package com.kapilguru.student.wallet.requestAmount

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.wallet.EarningsRepository


class RequestAmountViewModelFactory(private val apiHelper: ApiHelper,
                                    private val appliaction: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestAmountViewModel::class.java)) {
            return RequestAmountViewModel(
                EarningsRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}