package com.sharminnipu.scheduleapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id") val id:Int,
    @ColumnInfo(name="taskTile") val taskTile:String,
    @ColumnInfo(name="taskDate") val taskDate:String,
    @ColumnInfo(name="taskTime") val taskTime:String,
    @ColumnInfo(name="taskDetails") val taskDetails:String,
    @ColumnInfo(name="taskRemindBeforeTime") val taskRemindBeforeTime:Int,
    @ColumnInfo(name="taskReminderTime") val taskReminderTime:String,
    @ColumnInfo(name="alarmRequestCode") val alarmRequestCode:Int,

): Parcelable
