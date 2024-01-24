package com.example.mytodolist.model.task

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todoListTask")
data class Task_response(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val number:String,
    val title:String,
    val email:String,
    val selectItem:String,
    val formattedDate:String,
    val formattedTime:String,
    )
