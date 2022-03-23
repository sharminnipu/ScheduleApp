package com.sharminnipu.scheduleapp.data.roomDb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sharminnipu.scheduleapp.data.model.Task

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTask(): LiveData<List<Task>>
    @Query("SELECT * FROM tasks WHERE taskDate= :date")
    fun getTaskByDate(date:String): LiveData<List<Task>>
    @Update
    suspend fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTask()
}