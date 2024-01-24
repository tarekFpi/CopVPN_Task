package com.example.mytodolist.model.task

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userResponse")
data class User_response(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name:String,
    val email:String,
    val password:String,
    val comPass:String,

)
