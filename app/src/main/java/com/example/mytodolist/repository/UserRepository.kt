package com.example.mytodolist.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mytodolist.db.TaskDAo
import com.example.mytodolist.db.UserDAo
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.model.task.User_response
import com.example.mytodolist.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDAo: UserDAo){

    private var _mutableliveData = MutableLiveData<Resource<Boolean>>()
    val  userResponseLiveData : LiveData<Resource<Boolean>>
    get() =_mutableliveData

    suspend  fun  addUser(userResponse: User_response)=  userDAo.insert(userResponse)

    suspend  fun  delete(id:Int)=  userDAo.deleteByIds(id)



    suspend  fun  useLogin(email: String, password: String){

       val  result = userDAo.isLoginExist(email,password)

        Log.d("result:","${result}")

         if(result == true){

             _mutableliveData.postValue(Resource.Success(result))
         }else{

             _mutableliveData.postValue(Resource.Error("Login Failed.."))
         }

    }


}