package com.sharminnipu.scheduleapp.viewModel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.sharminnipu.scheduleapp.data.model.Task
import com.sharminnipu.scheduleapp.data.roomDb.TaskDatabase
import com.sharminnipu.scheduleapp.getOrAwaitValue
import junit.framework.TestCase
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TaskViewModelTest {
    private lateinit var viewModel: TaskViewModel
    private lateinit var database: TaskDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

   @Before
    fun setUp(){
       val context=ApplicationProvider.getApplicationContext<Context>()
       database = Room.inMemoryDatabaseBuilder(context,TaskDatabase::class.java).allowMainThreadQueries().build()

       viewModel= TaskViewModel()

   }

    @After
    fun dbClose(){
        database.close()
    }

    @Test
    fun insertEvent(){
        val context= ApplicationProvider.getApplicationContext<Context>()
        var model= Task(0,"Birthday","2021-03-23","10:00 PM","birthday party",5,"9:00 PM",123)
        viewModel.insertTask(context,model)
        val result=  viewModel.getAllTask(context)?.getOrAwaitValue()?.find {

            it.taskTile=="Birthday"
        }

        Truth.assertThat(result != null).isTrue()

    }

    @Test
    fun updateEvent(){
        val context=ApplicationProvider.getApplicationContext<Context>()
        var model= Task(1,"Birthday","2021-03-23","10:00 PM","birthday party",5,"9:00 PM",123)
        viewModel.insertTask(context,model)
        var modelUpdate=Task(1,"Birthday Party","2021-03-23","10:30 PM","birthday party",5,"9:00 PM",123)
        viewModel.updateTask(context,modelUpdate)
        val result=  viewModel.getAllTask(context)?.getOrAwaitValue()

        assertThat(result).contains(modelUpdate)
    }




    @Test
    fun deleteEvent(){
        val context=ApplicationProvider.getApplicationContext<Context>()
        var model= Task(3,"Birthday ","2021-03-23","10:00 PM","birthday party",5,"9:00 PM",123)
        viewModel.insertTask(context,model)
        viewModel.deleteTask(context,model)
        val result=  viewModel.getAllTask(context)?.getOrAwaitValue()

        assertThat(result).doesNotContain(model)

    }




}

