package com.sharminnipu.scheduleapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sharminnipu.scheduleapp.data.model.Task
import com.sharminnipu.scheduleapp.data.roomDb.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskRepository {
    companion object{

        private  var taskDatabase: TaskDatabase?=null

        private fun initialDB(context: Context):TaskDatabase?{

            return TaskDatabase.getInstance(context)
        }

        fun insert(context: Context, task:Task){

            taskDatabase= initialDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                taskDatabase?.getDao()?.insert(task)
            }
        }

        fun update(context: Context, task: Task){

            taskDatabase= initialDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                taskDatabase?.getDao()?.update(task)
            }
        }

        fun delete(context: Context, task: Task){

            taskDatabase= initialDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                taskDatabase?.getDao()?.delete(task)
            }
        }

        fun deleteAll(context: Context){

            taskDatabase= initialDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                taskDatabase?.getDao()?.deleteAllTask()
            }
        }
        fun getEventAll(context: Context): LiveData<List<Task>>? {
            taskDatabase= initialDB(context)
            return taskDatabase?.getDao()?.getAllTask()

        }


    }
}