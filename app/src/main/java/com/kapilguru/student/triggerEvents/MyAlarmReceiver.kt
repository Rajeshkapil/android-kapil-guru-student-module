package com.kapilguru.student.triggerEvents

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kapilguru.student.PARAM_TODAYS_SCHEDULE_NOTIFICATION
import com.kapilguru.student.R
import com.kapilguru.student.homeActivity.models.UpComingScheduleApi
import java.lang.StringBuilder

class MyAlarmReceiver : BroadcastReceiver() {

    var title: StringBuilder = StringBuilder("Please start live session")
    var body : StringBuilder = StringBuilder("for Batch")

    override fun onReceive(context: Context?, intent: Intent?) {
        onHandleIntent(context!!,intent)
    }

     fun onHandleIntent(context: Context, intent: Intent?) {
         val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

         val notificationId = 1
         val channelId = "channel-01"
         val channelName = "Channel Name"
         val importance = NotificationManager.IMPORTANCE_HIGH

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val mChannel = NotificationChannel(channelId, channelName, importance)
             notificationManager.createNotificationChannel(mChannel)
         }

         intent?.let {it->
            val scheduleUpComing =  it.getParcelableExtra<UpComingScheduleApi>(PARAM_TODAYS_SCHEDULE_NOTIFICATION)
             scheduleUpComing?.let { upcoming->
//                 Log.d("TAG", "onHandleIntent: $upcoming")
                 title = title.append(" "+upcoming.activityType)
                 body = body.append(" "+upcoming.code)
             }
         }

         val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
             .setSmallIcon(R.drawable.edit_icon)
             .setContentTitle(title)
             .setContentText(body)

         notificationManager.notify(notificationId, mBuilder.build())

    }

}