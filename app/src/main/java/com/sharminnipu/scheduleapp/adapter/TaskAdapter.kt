package com.sharminnipu.scheduleapp.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.data.model.Task
import kotlinx.android.synthetic.main.calender_view_activity.view.*
import kotlinx.android.synthetic.main.task_list_view.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter (val taskList:ArrayList<Task> ): RecyclerView.Adapter<TaskAdapter.FeedViewHolder>() {

   // var onItemAction: ((model: Task, position: Int) -> Unit)? = null
    var onTaskAction:((model: Task)-> Unit)?=null

    var context: Context?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedViewHolder {
        context=parent.context
        return FeedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_view, parent, false)

        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        val model = taskList[position]
        holder.tvTaskTitle.text=model.taskTile
        holder.tvTaskReminderTime.text=" Reminder Time: "+model.taskReminderTime
        holder.tvTaskDescription.text=model.taskDetails
        holder.tvTaskDate.text=model.taskDate
        holder.tvTaskTime.text=model.taskTime
        holder.ivBtnMore.setOnClickListener {
            onTaskAction?.invoke(model)
        }

        var simpleDateFormat=SimpleDateFormat("yyyy-MM-dd hh:mm a")

        var time=model.taskDate+" "+model.taskTime
        var rescheduleTime=model.taskDate+" "+model.taskReminderTime

        var currentTime=simpleDateFormat.format(Date())
        var appointmentCreateTimeInDate=simpleDateFormat.parse(time)
        var appointmentRescheduleTimeInDate=simpleDateFormat.parse(rescheduleTime)
        var currentTimeInDate=simpleDateFormat.parse(currentTime)
        Log.e("testing appointment time",appointmentCreateTimeInDate.toString())
        Log.e("testingcurrent time",currentTimeInDate.toString())



          if(currentTimeInDate.equals(appointmentCreateTimeInDate) ||currentTimeInDate.after(appointmentCreateTimeInDate)){
              holder.ivBtnMore.visibility=View.GONE
              holder.tvTaskStatus.text="Completed"
              holder.ivTaskStatus.setColorFilter(Color.GREEN)
              holder.tvTaskStatus.setTextColor(Color.GREEN)
           }else if(currentTimeInDate.before(appointmentCreateTimeInDate)){

               if(currentTimeInDate.equals(appointmentRescheduleTimeInDate)||currentTimeInDate.after(appointmentCreateTimeInDate)){
                  holder.ivBtnMore.visibility=View.GONE
                  holder.tvTaskStatus.text="Upcoming"
                  holder.ivTaskStatus.setImageResource(R.drawable.ic_circle)
                  holder.tvTaskStatus.setTextColor(Color.parseColor("#FFC107"))
              }else{
                   holder.ivBtnMore.visibility=View.VISIBLE
                   holder.tvTaskStatus.text="Upcoming"
                   holder.ivTaskStatus.setImageResource(R.drawable.ic_circle)
                   holder.tvTaskStatus.setTextColor(Color.parseColor("#FFC107"))
               }

          }



    }

    inner class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskTitle= view.tvEventTitle!!
        val tvTaskDescription= view.tvEventDescription!!
        val tvTaskReminderTime=view.tvEventReminderTime!!
        val tvTaskDate= view.tvEventDate!!
        val tvTaskTime= view.tvEventTime!!
        val tvTaskStatus= view.tvEventStatus!!
        val ivTaskStatus=view.ivEventStatus!!
        val ivBtnMore=view.moreOptionBtn!!







    }




}