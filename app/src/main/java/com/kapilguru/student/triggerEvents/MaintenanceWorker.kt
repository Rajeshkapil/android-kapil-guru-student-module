package com.kapilguru.student.triggerEvents

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kapilguru.student.ApiHelper
import com.kapilguru.student.MyApplication
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.ui.home.UpComingScheduleResponse
import kotlinx.coroutines.*

class MaintenanceWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    val TAG = "MaintainanceWorker"
    var abc = 1

    override suspend fun doWork(): Result {
        fetchUpComingClasses()
        return Result.success()
    }

    private suspend fun fetchUpComingClasses()  {
        val apiUpcomingScheduleResponse: Deferred<UpComingScheduleResponse> = GlobalScope.async {
            val trainerId = StorePreferences(appContext).studentId
            val apiHelper = ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE)
            apiHelper.fetchUpcomingSchedule(trainerId.toString())
        }
        val abc = apiUpcomingScheduleResponse.await()
        abc.data?.let {(appContext.applicationContext as MyApplication).getPendingIntent(it)}
    }
}