package com.kapilguru.student.wallet

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.wallet.requestAmount.RequestMoneyApi


class EarningsRepository(private val apiHelper: ApiHelper) {


    suspend fun getEarnings(studentId: String)=apiHelper.getEarnings(studentId)

    suspend fun requestMoney(requestMoney: RequestMoneyApi)=apiHelper.requestMoney(requestMoney)

}