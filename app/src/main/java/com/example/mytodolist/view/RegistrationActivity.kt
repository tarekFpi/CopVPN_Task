package com.example.mytodolist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.R
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.model.task.User_response
import com.example.mytodolist.viewmodel.TaskViewModel
import com.example.mytodolist.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {


    private  lateinit var edit_userName: EditText

    private  lateinit var edit_email: EditText

    private  lateinit var edit_pass: EditText

    private  lateinit var edit_Compass: EditText

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        userViewModel= ViewModelProvider(this)[UserViewModel::class.java]


        edit_userName =findViewById(R.id.edit_regName)

        edit_email = findViewById(R.id.edit_regEmail)

        edit_pass = findViewById(R.id.edit_regPass)

        edit_Compass =findViewById(R.id.edit_regComPass)

    }

    fun Registration_button(view: View) {

        if(edit_userName.text.isEmpty()){
            Snackbar.make(view, "Name is Empty..", Snackbar.LENGTH_LONG).show()
            edit_userName.error = "User Name is Empty.."

        }else if(edit_email.text.isEmpty()){

            Snackbar.make(view, "Email is Empty..", Snackbar.LENGTH_LONG).show()
            edit_email.error = "Email is Empty.."

        }else if(edit_pass.text.isEmpty()){

            Snackbar.make(view, "Password is Empty..", Snackbar.LENGTH_LONG).show()
            edit_pass.error = "Password is Empty.."

        }else if(edit_Compass.text.isEmpty()){

            Snackbar.make(view, "Confirm is Empty..", Snackbar.LENGTH_LONG).show()
            edit_Compass.error = "Confirm is Empty.."

        }else if(edit_Compass.text.toString() != edit_pass.text.toString()){

            Snackbar.make(view, "Password And Confirm Not Match..", Snackbar.LENGTH_LONG).show()
            edit_Compass.error = "Password And Confirm Not Match.."
            edit_pass.error = "Password And Confirm Not Match.."

        }else{

            val name = edit_userName.text.toString()

            val email =  edit_email.text.toString()

            val password = edit_pass.text.toString()

            val comPass = edit_Compass.text.toString()

            val userResponse = User_response(null,name,email,password,comPass)
            userViewModel.addUser(userResponse)

            Toast.makeText(applicationContext, "Registration SuccessFull..", Toast.LENGTH_SHORT).show()

            edit_userName.setText("")
            edit_email.setText("")
            edit_pass.setText("")
            edit_Compass.setText("")
        }
    }

    fun gotoLogin(view: View) {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}