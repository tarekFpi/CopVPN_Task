package com.example.mytodolist.model.task

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "taskManager")
data class Task_response(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title:String,
    val details:String,
    val formattedDate:String,
    val formattedTime:String,
    )
