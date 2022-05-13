package com.kapilguru.student.wallet.earningsDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.wallet.model.EarningsRefundList

class AmountViewModel(private val earningsDetailsRepository: AmountDetailsRepository,
                      application: Application):AndroidViewModel(application) {

    var trainerId: Int
    var earningsRefundList: MutableLiveData<ArrayList<EarningsRefundList>>  = MutableLiveData()
    var studentId: MutableLiveData<String> = MutableLiveData()
    var studentName: MutableLiveData<String> = MutableLiveData()

    init
    {
        val pref = StorePreferences(application)
        trainerId = pref.studentId
    }


}