package com.sharminnipu.scheduleapp.view.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.data.model.Task
import com.sharminnipu.scheduleapp.utils.AppConstant
import com.sharminnipu.scheduleapp.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*


class AddEventActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private var reminderId=0//no remind
    private  var data:Task?=null
    private lateinit  var myCalendar: Calendar
    private lateinit  var  am_pm:String
    var reminderTime=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)


        initialView()
    }

    private fun initialView(){
        viewModel= ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.createNotificationChannel(this)
        loadReminderDropDownData()
        if(AppConstant.isUpdateEvent){

            data=intent.getParcelableExtra<Task>("editTask")
            Log.e("update",data.toString())
            setupData(data!!)
            loadReminderDropDownData()
        }
      //  backBtn.setOnClickListener {
          //  finish()
      //  }
        saveBtn.setOnClickListener {
            if(AppConstant.isUpdateEvent){
               updateDataToLocalDB(data!!)
            }else{
                saveDataToLocalDB()
            }

        }

        dateAndTimePickerDialog()


    }

    private fun updateDataToLocalDB(data:Task) {
        val getDate=etEventDate.text.toString().trim()
        val getTime=etEventTime.text.toString().trim()
        val getTitle=etEventTitle.text.toString().trim()
        val getEventDes=etEventDes.text.toString().trim()

       // var timeFormat= SimpleDateFormat("HH:mm a").parse(getTime)


        val minute =myCalendar[Calendar.MINUTE].minus(reminderId)
        Log.e("minute",minute.toString())
        val hour = myCalendar[Calendar.HOUR_OF_DAY]
        val day = myCalendar[Calendar.DAY_OF_MONTH]
        val month = myCalendar[Calendar.MONTH]
        val year = myCalendar[Calendar.YEAR]

        myCalendar.set(year, month, day, hour, minute)

        reminderTime=SimpleDateFormat("hh:mm a").format(myCalendar.time)

        Log.e("hour minu:",reminderTime)

        if(!TextUtils.isEmpty(getDate) && !TextUtils.isEmpty(getTime) && !TextUtils.isEmpty(getTitle)){


            var update= viewModel.updateTask(this,Task(data.id,getTitle,getDate,getTime,getEventDes,reminderId,reminderTime,data.alarmRequestCode))
            if (update){
                if(getDate!=data.taskDate || getTime!=data.taskTime){
                    var alarm=viewModel.setAlarm(data.alarmRequestCode,this,myCalendar,getTitle,getEventDes)
                    if (alarm){
                        Toast.makeText(this,"Your event updated successfully with reminder!!", Toast.LENGTH_LONG).show()
                        finish()
                    }

                }else{
                    Toast.makeText(this,"Your event updated successfully!!", Toast.LENGTH_LONG).show()
                    finish()
                }


            }


        }else
        {
            Toast.makeText(this,"Please fill up the flied", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupData(data:Task){
        saveBtn.text="Update"
        titleEventLayout.text="Update Your event"
        etEventDate.setText(data.taskDate)
        etEventTime.setText(data.taskTime)
        etEventTitle.setText(data.taskTile)
        etEventDes.setText(data.taskDetails)
        reminderTime=data.taskReminderTime
        when (data.taskRemindBeforeTime) {
            5 -> {
                etRemindMeAt.setText("5 min before")
                reminderId=5
            }
            10 -> {
                etRemindMeAt.setText("10 min before")
                reminderId=10
            }
            15 -> {
                etRemindMeAt.setText("15 min before")
                reminderId=15
            }
        }



    }

    private fun dateAndTimePickerDialog(){

         myCalendar = Calendar.getInstance()

        val datePickerListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    //  val myFormat = "hh:mm a"
                    //  val myFormat = "dd/MM/yyyy" //In which you need put here
                    //  val sdf = SimpleDateFormat(myFormat, Locale.getDefault()).format(Date())
                    etEventDate.setText(SimpleDateFormat("yyyy-MM-dd").format(myCalendar.time))
                }
        etEventDate.setOnClickListener {
            DatePickerDialog(this, datePickerListener, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]).show() }
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            etEventTime.setText(
                    SimpleDateFormat("hh:mm a").format(myCalendar.time)
            )

        }
        etEventTime.setOnClickListener {
            TimePickerDialog(this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(
                    Calendar.MINUTE), false).show() }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveDataToLocalDB(){
        Log.e("reminderId",reminderId.toString())
        val getDate=etEventDate.text.toString().trim()
        val getTime=etEventTime.text.toString().trim()
        val getTitle=etEventTitle.text.toString().trim()
        val getEventDes=etEventDes.text.toString().trim()


        val minute =myCalendar[Calendar.MINUTE].minus(reminderId)
        Log.e("minute",minute.toString())
        val hour = myCalendar[Calendar.HOUR_OF_DAY]
        val day = myCalendar[Calendar.DAY_OF_MONTH]
        val month = myCalendar[Calendar.MONTH]
        val year = myCalendar[Calendar.YEAR]


        myCalendar.set(year, month, day, hour, minute)
        reminderTime=SimpleDateFormat("hh:mm a").format(myCalendar.time)
        // reminderTime="$hour:$minute $am_pm"
        Log.e("hour minu:",reminderTime)

        if(!TextUtils.isEmpty(getDate) && !TextUtils.isEmpty(getTime) && !TextUtils.isEmpty(getTitle)){
            val random = Random()
            var requestCode=random.nextInt(999 - 100) + 100
             Log.e("requestCode",requestCode.toString())

            var inserted= viewModel.insertTask(this,Task(0,getTitle,getDate,getTime,getEventDes,reminderId,reminderTime,requestCode))
            if (inserted){

              //  Log.e("requestCode",requestCode.toString())
                var alarm=viewModel.setAlarm(requestCode,this,myCalendar,getTitle,getEventDes)
                if (alarm){
                   Toast.makeText(this,"Your event inserted successfully with reminder", Toast.LENGTH_LONG).show()
                    finish()
                }

               }
           }else
              {
            Toast.makeText(this,"Please fill up the flied", Toast.LENGTH_LONG).show()
             }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(AppConstant.isUpdateEvent){
            AppConstant.isUpdateEvent =false
        }

    }
    private fun loadReminderDropDownData(){
        val reminderTime=resources.getStringArray(R.array.alarmTime)
        val arrayAdapter= ArrayAdapter(this, R.layout.drop_down_item,reminderTime)
        etRemindMeAt.setAdapter(arrayAdapter)
          etRemindMeAt.setOnItemClickListener { parent, view, position, id ->
              reminderId = (position+1)
              when (reminderId) {
                  1 -> {
                      reminderId=5
                  }
                  2 -> {
                      reminderId=10
                  }
                  3 -> {
                      reminderId=15
                  }
              }
              Log.e("dropdownItem",(position+1).toString())
       }

      // Log.d("dropdownItem",etRemindMeAt.text.toString())
    }


}


