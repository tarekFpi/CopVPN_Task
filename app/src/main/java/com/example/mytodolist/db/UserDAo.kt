package com.example.mytodolist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mytodolist.model.task.User_response
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(userResponse: User_response)


    @Query("DELETE  FROM userResponse  WHERE id =:id")
    suspend  fun deleteByIds(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM userResponse WHERE email = :email AND password =:password)")
    suspend fun isLoginExist(email : String,password:String) : Boolean

/*
    @Query("SELECT *FROM userResponse ORDER BY id DESC")
    fun showUser(): Flow<User_response>
*/

}