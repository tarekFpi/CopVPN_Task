package com.example.mytodolist.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.R
import com.example.mytodolist.utils.CheckInternetConnection
import com.example.mytodolist.utils.Resource
import com.example.mytodolist.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private  lateinit var edit_email: EditText

    private  lateinit var edit_pass: EditText

    private lateinit var  progressBar:ProgressDialog

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    private lateinit var rememberme: CheckBox

    private var pass: String? = null

    private var email: String? = null

    var preferences: SharedPreferences? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


         preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)


        progressBar = ProgressDialog(this)
        progressBar.setMessage("please wait...")

        userViewModel= ViewModelProvider(this)[UserViewModel::class.java]

        edit_email =findViewById(R.id.edit_logName)

        edit_pass = findViewById(R.id.edit_logPass)

        rememberme = findViewById(R.id.rememberMe)


    }

    override fun onResume() {
        super.onResume()

        internetConnection()

        pass = preferences?.getString("savePass", "")

        email = preferences?.getString("saveEmail", "")

        if (pass != "" && email != "") {
            rememberme.isChecked = true

            edit_email.setText(email)
            edit_pass.setText(pass)

        }
    }

    private  fun internetConnection(){

        if (!checkInternetConnection.isInternetAvailable(this))

            Toast.makeText(applicationContext, "No Internet", Toast.LENGTH_SHORT).show()


    }

    fun gotoRegistration(view: View) {

        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    fun login_button(view: View) {

        if(edit_email.text.isEmpty()){

            Snackbar.make(view, "Email is Empty..", Snackbar.LENGTH_LONG).show()
            edit_email.error = "Email is Empty.."

        }else if(edit_pass.text.isEmpty()){

            Snackbar.make(view, "Password is Empty..", Snackbar.LENGTH_LONG).show()
            edit_pass.error = "Password is Empty.."

        }else{

            progressBar.show()

            userViewModel.loginRequest(edit_email.text.toString().trim(),edit_pass.text.toString().trim())

            bindObservers()
        }


    }

    @SuppressLint("CommitPrefEdits")
    private fun  bindObservers(){

        userViewModel.userLogLiveData.observe(this, Observer {

            when (it) {

                is Resource.Loading ->{

                    progressBar.show()
                }

                is Resource.Success -> {

                    Toast.makeText(applicationContext,"User logged in successfully",Toast.LENGTH_SHORT).show()

                     notificationShow("User logged in successfully")

                    preferences?.edit()?.putString("saveEmail",edit_email.text.toString().trim())
                        ?.apply()

                    preferences?.edit()?.putString("savePass",edit_pass.text.toString().trim())
                        ?.apply()


                    edit_email.setText("")

                    edit_pass.setText("")
                    val intent = Intent(this, TaskListActivity::class.java)
                    startActivity(intent)
                    this.finish()

                    progressBar.dismiss()

                }
                is Resource.Error -> {
                    progressBar.dismiss()
                    Toast.makeText(applicationContext,"Error ${it.message}",Toast.LENGTH_SHORT).show()
                }

            }

        })
    }


    @SuppressLint("MissingPermission")
    private fun notificationShow(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("Channel", "Channel", NotificationManager.IMPORTANCE_HIGH)
            val manager = applicationContext.getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "Channel")
                .setContentTitle("Login Screen")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentText(message)
                .setAutoCancel(true) //.setSound(Uri.)
                .setWhen(System.currentTimeMillis())
        val notificationManagerCompat = NotificationManagerCompat.from(
            applicationContext
        )
        notificationManagerCompat.notify(1, notification.build())
    }
}