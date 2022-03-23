package com.sharminnipu.scheduleapp.viewModel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.broadcastReceiver.AlarmBroadcast
import com.sharminnipu.scheduleapp.data.model.Task
import com.sharminnipu.scheduleapp.repository.TaskRepository
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*

class TaskViewModel:ViewModel() {


    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingInt: PendingIntent

    fun insertTask(context: Context,task:Task):Boolean{

        TaskRepository.insert(context,task)
        return true
    }
    fun updateTask(context: Context,task:Task):Boolean{
        TaskRepository.update(context,task)

        return true
    }
    fun deleteTask(context: Context,task:Task):Boolean{
        TaskRepository.delete(context,task)
        return true
    }
    fun getAllTask(context: Context): LiveData<List<Task>>? {

        return TaskRepository.getEventAll(context)
    }
    fun setAlarm(requestCode:Int,context: Context,calendar: Calendar,taskTitle:String,taskDetails:String):Boolean {

        alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(context, AlarmBroadcast::class.java)
        intent.putExtra("requestCode",requestCode)
        intent.putExtra("title",taskTitle)
        intent.putExtra("details",taskDetails)

        pendingInt= PendingIntent.getBroadcast(context,requestCode,intent,0)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    //  AlarmManager.INTERVAL_DAY,
                    pendingInt
            )
        }

         return  true

    }
    fun cancelAlarm(requestCode:Int,context: Context):Boolean{
        alarmManager=context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent=Intent(context,AlarmBroadcast::class.java)
        intent.putExtra("requestCode",requestCode)
         pendingInt= PendingIntent.getBroadcast(context,requestCode,intent,0)
         alarmManager.cancel(pendingInt)

         return true
    }
    fun createNotificationChannel(context: Context){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            val name:CharSequence="notificationChannel"
            val descriptiop="channel for alarm"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel("channelforandroid",name,importance)
            channel.description=descriptiop
            val notificationManager=context.getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)

        }
    }



}