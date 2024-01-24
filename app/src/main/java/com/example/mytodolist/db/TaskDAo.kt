package com.example.mytodolist.db
import androidx.room.*
import com.example.mytodolist.model.task.Task_response
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(task: Task_response)

    @Update
    suspend fun update(task: Task_response)

    @Delete
    suspend  fun delete(task: Task_response)

    @Query("DELETE  FROM todoListTask  WHERE id =:id")
    suspend  fun deleteByIds(id: Int)

    @Query("SELECT *FROM todoListTask ORDER BY id DESC")
    fun showData(): Flow<List<Task_response>>


}