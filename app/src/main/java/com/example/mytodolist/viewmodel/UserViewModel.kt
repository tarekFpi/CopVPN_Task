package com.example.mytodolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.model.task.User_response
import com.example.mytodolist.repository.TaskRepository
import com.example.mytodolist.repository.UserRepository
import com.example.mytodolist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository
) : ViewModel(){


    val userLogLiveData : LiveData<Resource<Boolean>>
        get() = userRepository.userResponseLiveData

      fun loginRequest(email: String, password: String){

        viewModelScope.launch(Dispatchers.IO){

            userRepository.useLogin(email,password)

        }
    }



    fun addUser(userResponse: User_response){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.addUser(userResponse)

        }
    }



    fun deleteTask(id:Int){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.delete(id)
        }
    }

}