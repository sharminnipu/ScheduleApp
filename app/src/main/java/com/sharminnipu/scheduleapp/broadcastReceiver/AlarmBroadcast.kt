package com.sharminnipu.scheduleapp.broadcastReceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.view.home.HomeActivity
import java.util.*

class AlarmBroadcast:BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder
    val channelId="channelforandroid"
    var requestCode:Int=0
    var notificationTile=""
    var notificationDetails=""
    override fun onReceive(context: Context?, intent: Intent?) {
        requestCode= intent!!.getIntExtra("requestCode", 0)
        notificationTile= intent!!.getStringExtra("title").toString()
        notificationDetails= intent!!.getStringExtra("details").toString()



        val i=Intent(context,HomeActivity::class.java)
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent= PendingIntent.getActivity(context, requestCode,i,0)

        notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val random = Random()
        // val m=1234
        val m= random.nextInt(9999 - 1000) + 1000

        builder= NotificationCompat.Builder(context, channelId)
                .setContentTitle("$notificationTile")
                .setContentText("$notificationDetails")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        notificationManager.notify(m, builder.build())
    }
}