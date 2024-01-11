package com.example.mytodolist.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.repository.TaskRepository
import com.example.mytodolist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository
                                           ) : ViewModel() {

    private var _response = MutableLiveData<Resource<List<Task_response>>>(Resource.Loading())
    val postResponse: LiveData<Resource<List<Task_response>>> = _response


    init {
        getTaskList()

    }

    private fun getTaskList(){
        viewModelScope.launch(Dispatchers.Main){
            taskRepository.showTaskList()
                .collect {
                    _response.postValue(it)
                }
        }
    }



    fun addTask(task: Task_response){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.addTask(task)
            getTaskList()
        }
    }

    fun updateTask(task: Task_response){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.update(task)
        }
    }

    fun deleteTask(id:Int){
        viewModelScope.launch(Dispatchers.IO){
            taskRepository.delete(id)
        }
    }

    }
