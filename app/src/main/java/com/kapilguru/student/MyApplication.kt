package com.kapilguru.student

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi
import com.kapilguru.student.network.ApiResource
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.preferences.StorePreferences
import com.kapilguru.student.triggerEvents.MaintenanceWorker
import com.kapilguru.student.triggerEvents.MyAlarmReceiver
import com.kapilguru.student.webinar.WebinarRepository
import com.kapilguru.student.webinar.model.LanguageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jitsi.meet.sdk.VideomeetSDK
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    private val TAG = "MyApplication"
    var mLanguagesList = ArrayList<LanguageData>()


    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initVideoMeet()
        fetchLanguages()
    }

    //init videoMeet
    private fun initVideoMeet(){
        try{
            VideomeetSDK.initSDK(VideomeetSDK.Configuration.Builder(this).setLicenseKey(VIDEO_MEET_LICENSE_KEY).setHostName(VIDEO_MEET_HOST_NAME).build())
        }catch (exception : RuntimeException){
            exception.printStackTrace()
        }
    }


    fun getLanguages(listener : FetchLanguagesListener) {
        if (mLanguagesList.isEmpty()) {
            fetchLanguages(listener)
        } else {
            listener.onLanguagesFetched(mLanguagesList)
        }
    }

    private fun fetchLanguages(listener : FetchLanguagesListener?=null){
        val addGuestLectureRepository = WebinarRepository(ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                mLanguagesList = addGuestLectureRepository.getCourseLanguage().languageData
            } catch (exception: RetrofitNetwork.NetworkConnectionError) {
                showErrorDialog()
//                profileDataResponse.postValue(ApiResource.error(data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: IOException) {
//                profileDataResponse.postValue(ApiResource.error(code = 900, data = null, message = exception.message ?: "Error Occurred!"))
            }  catch (exception: HttpException) {
//                profileDataResponse.postValue(ApiResource.error(code = exception.code(), data = null, message = exception.message ?: "Error Occurred!"))
            } catch (exception: Exception) {
//                profileDataResponse.postValue(ApiResource.error(code = 500, data = null, message = exception.message ?: "Error Occurred!"))
            }
            listener?.onLanguagesFetched(mLanguagesList)
        }
    }

    private fun showErrorDialog() {
//       showSingleButtonErrorDialog(context, getString(R.string.network_connection_error))
    }

    interface FetchLanguagesListener{
        fun onLanguagesFetched(languagesDataList : ArrayList<LanguageData>)
    }

    fun initMaintenanceWorker() {
        if (StorePreferences(this).studentId == 0) {
            return
        }
        val workRequest = PeriodicWorkRequestBuilder<MaintenanceWorker>(15, TimeUnit.MINUTES).setInitialDelay(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Maintenance", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    fun getPendingIntent(upComingSchedule: List<UpComingScheduleApi>) {
        getUpComingScheduleInOneHour(upComingSchedule)
    }

    private fun getUpComingScheduleInOneHour(upComingSchedule: List<UpComingScheduleApi>) {
        for(schedule in upComingSchedule) {
            if(schedule.isAboutToLiveInOneHour()) {
                setUpcomingAlarm(schedule)
            }
        }
    }

    private fun setUpcomingAlarm(schedule: UpComingScheduleApi) {
        val notifyIntent = Intent(this, MyAlarmReceiver::class.java)
        notifyIntent.putExtra(PARAM_TODAYS_SCHEDULE_NOTIFICATION,schedule)
        sendBroadcast(notifyIntent)
        val pendingIntent = PendingIntent.getBroadcast(context, 50, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val timeInTimeMillis = schedule.startTime!!.getEpochTime()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInTimeMillis, pendingIntent)
    }

}


