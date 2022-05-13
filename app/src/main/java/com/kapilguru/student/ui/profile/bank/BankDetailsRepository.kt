package com.kapilguru.student.ui.profile.bank

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.ui.profile.bank.data.BankDetailsUploadRequest

class BankDetailsRepository(private val apiHelper: ApiHelper) {
    suspend fun getBankDetails(userId : String) = apiHelper.getBankDetails(userId)
    suspend fun saveBakDetails(bankDetailsUploadRequest: BankDetailsUploadRequest) = apiHelper.saveBankDetails(bankDetailsUploadRequest)
    suspend fun updateBakDetails(bankId : String, bankDetailsUploadRequest: BankDetailsUploadRequest) = apiHelper.updateBankDetails(bankId,bankDetailsUploadRequest)
}