package com.example.mytodolist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytodolist.model.task.Task_response


@Database(entities = [Task_response::class], version = 1)
 abstract class TaskDatabase : RoomDatabase(){

  abstract fun getTaskDao(): TaskDAo
}