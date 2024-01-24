package com.example.mytodolist.view
import android.annotation.SuppressLint
import android.app.*
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.R
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.utils.CheckInternetConnection
import com.example.mytodolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    private  lateinit var edit_number: EditText

    private  lateinit var edit_title: EditText

    private  lateinit var edit_email: EditText

    private  lateinit var text_date: TextView

    private  lateinit var text_time: TextView

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var toolbar: Toolbar

    private  lateinit var  formattedDate:String

    private  lateinit var  formattedTime:String

    private  lateinit var  spinnerTask: Spinner

    private var itemposition = ""

    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        toolbar = findViewById(R.id.appbar_taskAdd)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
         val upArrow = resources.getDrawable(R.drawable.back)
        upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)

        taskViewModel= ViewModelProvider(this)[TaskViewModel::class.java]

        edit_number =findViewById(R.id.edit_taskNumber)

        edit_title =findViewById(R.id.edit_taskTitle)

        edit_email =findViewById(R.id.edit_taskEmail)

        text_date =findViewById(R.id.add_taskDate)

        text_time =findViewById(R.id.add_taskTime)

        spinnerTask=findViewById(R.id.taskCategory_Spinner)


        spinnerTask.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                itemposition = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        text_time.setOnClickListener {

            showTimePicker()
        }

        text_date.setOnClickListener {

        showDatePicker()

        }

    }

    override fun onResume() {
        super.onResume()

        internetConnection()
    }

    private  fun internetConnection(){

        if (!checkInternetConnection.isInternetAvailable(this))

            Toast.makeText(applicationContext, "No Internet", Toast.LENGTH_SHORT).show()


    }



    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                  formattedDate = dateFormat.format(selectedDate.time)

                text_date.text = "$formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
}

    private fun showTimePicker() {
        // Create a TimePickerDialog

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this,
            { view, hourOfDay, minute ->
               // formattedTime =  String.format("%d : %d", hourOfDay, minute)

                mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                mcurrentTime.set(Calendar.MINUTE, minute)
                val mSDF = SimpleDateFormat("hh:mm a")
                  formattedTime = mSDF.format(mcurrentTime.getTime())

                text_time.text =formattedTime
            }, hour, minute, false)

        mTimePicker.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save_button(view: View) {

         if(edit_number.text.isEmpty()){
            Snackbar.make(view, "Number is Empty..", Snackbar.LENGTH_LONG).show()

        }else if(edit_title.text.isEmpty()){
            Snackbar.make(view, "Title is Empty..", Snackbar.LENGTH_LONG).show()

        }else if(edit_email.text.isEmpty()){
            Snackbar.make(view, "Email is Empty..", Snackbar.LENGTH_LONG).show()

        }else  if (itemposition=="Selected Item") {

        Snackbar.make(view, "Dropdown Category Item Empty..", Snackbar.LENGTH_LONG).show()

        } else if(text_date.text.isEmpty()){

            Snackbar.make(view, "Date is Empty..", Snackbar.LENGTH_LONG).show()
        } else if(text_time.text.isEmpty()){

             Snackbar.make(view, "Time is Empty..", Snackbar.LENGTH_LONG).show()
         }else{


             val number =edit_number.text.toString()

             val title =edit_title.text.toString()

             val email =edit_email.text.toString()

              val taskResponse = Task_response(null,number,title,email,itemposition,formattedDate,formattedTime)
             taskViewModel.addTask(taskResponse)

            edit_number.setText("")
            edit_title.setText("")
            edit_email.setText("")
            spinnerTask.setSelection(0)
            text_date.text = ""
            text_time.text = ""

             notificationShow("Save SuccessFull..")
            Toast.makeText(applicationContext, "Save SuccessFull..", Toast.LENGTH_SHORT).show()


        }

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
                .setContentTitle("Add Screen")
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