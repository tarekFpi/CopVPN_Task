package com.example.mytodolist.db
import androidx.room.*
import com.example.mytodolist.model.task.Task_response
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAo {

    @Insert
    suspend  fun Insert(task: Task_response)

    @Update
    suspend fun Update(task: Task_response)

    @Delete
    suspend  fun Delete(task: Task_response)

    @Query("DELETE  FROM taskManager  WHERE id =:id")
    suspend  fun DeleteByIds(id: Int)

    @Query("SELECT *FROM taskManager ORDER BY id DESC")
    fun ShowData(): Flow<List<Task_response>>
}