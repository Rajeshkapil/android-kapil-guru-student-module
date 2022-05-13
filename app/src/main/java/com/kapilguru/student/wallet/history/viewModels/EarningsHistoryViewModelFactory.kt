package com.kapilguru.student.wallet.history.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.wallet.history.EarningsHistoryRepository

class EarningsHistoryViewModelFactory(private val apiHelper: ApiHelper,
                                      private val appliaction: Application
): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EarningsHistoryViewModel::class.java)) {
            return EarningsHistoryViewModel(
                EarningsHistoryRepository(apiHelper),
                appliaction) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}