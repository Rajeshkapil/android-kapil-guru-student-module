package com.kapilguru.student.wallet.history

import com.kapilguru.student.ApiHelper

class EarningsHistoryRepository(private val apiHelper: ApiHelper) {

    suspend fun getEarningsHistory(trainerId: String) = apiHelper.getEarningsHistory(trainerId)

    suspend fun getHistoryDetailAmount(trainerId: String,selectedId: String) = apiHelper.getHistoryDetailAmount(trainerId,selectedId)

//    http://35.174.62.203:9000/trainer/getRequestedDetails/5

}