package com.kapilguru.student.payment

import com.kapilguru.student.ApiHelper
import com.kapilguru.student.payment.model.InitiateTransactionRequest
import com.kapilguru.student.payment.model.TransactionStatusRequest

class PaymentRepository(private val apiHelper: ApiHelper) {
    suspend fun callInitiationTransactionRequest(initiateTransactionRequest: InitiateTransactionRequest) =
        apiHelper.callInitiationTransactionApi(initiateTransactionRequest)

    suspend fun callTransactionStatusRequest(initiateTransactionRequest: TransactionStatusRequest) =
        apiHelper.callTransactionStatusApi(initiateTransactionRequest)
}