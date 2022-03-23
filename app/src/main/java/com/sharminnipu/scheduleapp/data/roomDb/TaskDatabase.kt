package com.sharminnipu.scheduleapp.data.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sharminnipu.scheduleapp.data.model.Task

@Database(entities = [Task::class],version = 2,exportSchema = false,)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun getDao():TaskDAO

    companion object{
        private const val DATABASE_NAME="TaskDatabase"

        @Volatile
        var instance:TaskDatabase?=null

        val migration_1_2:Migration=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
               database.execSQL("ALTER TABLE tasks ADD COLUMN taskReminderTime TEXT  NOT NULL DEFAULT '' ")
            }

        }

        fun getInstance(context: Context):TaskDatabase?{
            if(instance==null){
                synchronized(TaskDatabase::class.java){
                    if(instance==null){
                        instance= Room.databaseBuilder(context,TaskDatabase::class.java,
                            DATABASE_NAME)
                                .addMigrations(migration_1_2)
                                .allowMainThreadQueries()
                                .build()
                    }
                }

            }

            return instance

        }




    }
}