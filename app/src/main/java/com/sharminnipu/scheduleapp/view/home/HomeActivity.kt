package com.sharminnipu.scheduleapp.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sharminnipu.scheduleapp.R
import com.sharminnipu.scheduleapp.adapter.CalendarAdapter
import com.sharminnipu.scheduleapp.adapter.TaskAdapter
import com.sharminnipu.scheduleapp.data.model.Task
import com.sharminnipu.scheduleapp.databinding.ActivityHomeBinding
import com.sharminnipu.scheduleapp.databinding.ActivitySplashBinding
import com.sharminnipu.scheduleapp.utils.AppConstant
import com.sharminnipu.scheduleapp.utils.CalendarUtils.daysInWeekArray
import com.sharminnipu.scheduleapp.utils.CalendarUtils.monthYearFromDate
import com.sharminnipu.scheduleapp.utils.CalendarUtils.selectedDate
import com.sharminnipu.scheduleapp.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel:TaskViewModel
    private lateinit var eventList:ArrayList<Task>
    lateinit var adapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchLocalAllEvent()
        initialView()
       // setWeekView()

    }
    private fun fetchLocalAllEvent(){

        viewModel= ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.getAllTask(applicationContext)?.observe(this, Observer {

            if(it.isNotEmpty()){
                emptyMsg.visibility=View.GONE
                eventList.clear()
                eventList.addAll(it as ArrayList<Task>)
                Log.e("eventList",eventList.toString())
                loadAdapter(eventList)
            }
            else
            {
                emptyMsg.visibility=View.VISIBLE
            }

           // setWeekView(eventList)

        })
    }

    private fun loadAdapter(eventList:ArrayList<Task>){
        adapter = TaskAdapter(eventList)
        adapter.notifyDataSetChanged()

        val llm = GridLayoutManager(this, 1)
        llm.orientation = GridLayoutManager.VERTICAL
        taskRecyclerView.layoutManager = llm
        taskRecyclerView.adapter = adapter

        adapter.onTaskAction={model ->

           // OpenDialog(model)
            openBottomSheetDialog(model)
        }
    }
    private  fun openBottomSheetDialog(model:Task){

       val view:View=layoutInflater.inflate(R.layout.bottom_sheet_layout,null)
       val dialog=BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
       dialog.setContentView(view)
       dialog.show()
       view.taskNameTv.text=model.taskTile
       view.deleteBtn.setOnClickListener {
           dialog.dismiss()
           onDeleteAlertDialog(view,model)
       }
        view.editBtn.setOnClickListener {
            dialog.dismiss()
            AppConstant.isUpdateEvent=true
           startActivity(Intent(this,AddEventActivity::class.java).putExtra("editTask",model))

        }


    }
    private fun onDeleteAlertDialog(view: View,model:Task) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(view.context)

        // set title
        builder.setTitle("Event Delete")

        //set content area
        builder.setMessage("Are you sure?\nYou want to delete the event.If you delete this task, you will also lose your reminder, Do you want it?")

        //set negative button
        builder.setPositiveButton("Yes") { dialog, id ->
            var deleteData=viewModel.deleteTask(this,model)
            if(deleteData){
                viewModel.cancelAlarm(model.alarmRequestCode,this)
                eventList.remove(model)
                adapter.notifyDataSetChanged()

                Toast.makeText(this,"your event deleted successfully!!", Toast.LENGTH_SHORT).show()
            }



        }

        //set positive button
        builder.setNegativeButton("No") { dialog, id ->
            // User cancelled the dialog
            dialog.dismiss()

        }

        builder.show()


    }

     private fun initialView(){
        // selectedDate = LocalDate.now()
         eventList=ArrayList<Task>()
        // Log.e("selected date", selectedDate.toString())

         addEventBtn.setOnClickListener {
             startActivity(Intent(this, AddEventActivity::class.java))
         }


     } /*
     private fun  setWeekView(){
        binding.monthYearTV.text=monthYearFromDate(selectedDate!!)
         var days:ArrayList<LocalDate?> =daysInWeekArray(selectedDate!!)

         adapter = CalendarAdapter(days)
         adapter.notifyDataSetChanged()

         val llm = GridLayoutManager(this, 7)
         llm.orientation = GridLayoutManager.VERTICAL
         calendarRecyclerView.layoutManager = llm
         calendarRecyclerView.adapter = adapter


     }
     fun previousWeekAction(view: View) {

         selectedDate = selectedDate!!.minusWeeks(1)
         setWeekView()
     }
     fun nextWeekAction(view: View) {
         selectedDate = selectedDate!!.plusWeeks(1)
         setWeekView()
     }  */
}


