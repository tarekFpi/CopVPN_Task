package com.example.mytodolist.repository

import com.example.mytodolist.db.TaskDAo
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class TaskRepository  @Inject constructor(private val contactDAo: TaskDAo){


suspend  fun  addTask(task: Task_response)=  contactDAo.Insert(task)

suspend  fun  update(task: Task_response)=  contactDAo.Update(task)

suspend  fun  delete(id:Int)=  contactDAo.DeleteByIds(id)


    fun showTaskList() : Flow<Resource<List<Task_response>>> = flow {

        emit(Resource.Loading())

        contactDAo.ShowData().collect {
            emit(Resource.Success(it))
        }
    }.catch {
        emit(Resource.Error(it.message ?: "Unknown Error"))
    }.flowOn(Dispatchers.IO)


}