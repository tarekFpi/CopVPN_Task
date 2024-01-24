package com.example.mytodolist.view
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.R
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateActivity : AppCompatActivity() {

    private  lateinit var edit_title: EditText

    private  lateinit var edit_email: EditText

    private lateinit var  progressDialog: ProgressDialog

    private lateinit var addTaskViewModel: TaskViewModel

    private  lateinit var text_date: TextView

    private  lateinit var text_time: TextView

    private var updateId=0

    private lateinit var toolbar: Toolbar

    private  lateinit var  formattedDate:String

    private  lateinit var  formattedTime:String

    private val calendar = Calendar.getInstance()

    private var numbertask=""

    private var ItemPosition=""

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        toolbar = findViewById(R.id.toolbar_updateActiviey)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val upArrow = resources.getDrawable(R.drawable.back)
        upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)

        addTaskViewModel= ViewModelProvider(this)[TaskViewModel::class.java]

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("please wait...")

        edit_title =findViewById(R.id.edit_taskUpTitle)

        edit_email =findViewById(R.id.edit_taskUpEmail)

        text_date =findViewById(R.id.edit_taskUpdateDate)

        text_time =findViewById(R.id.edit_taskUpdateTime)

        text_date.setOnClickListener {

            showDatePicker();
        }

        text_time.setOnClickListener {

            showTimePicker()

        }

        getData()
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


    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time)

                text_date.setText("$formattedDate")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getData(){

        val bundle :Bundle ?=intent.extras

        val title:String = bundle?.getString("title").toString()

        updateId  = bundle?.getInt("id")!!

        val email:String = bundle.getString("email").toString()

        numbertask = bundle.getString("number").toString()

        ItemPosition = bundle.getString("item").toString()

        edit_title.setText(title)

        edit_email.setText(email)

    }

    fun update_button(view: View) {


        if(edit_title.text.isEmpty()){
            Snackbar.make(view, "Title is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(edit_email.text.isEmpty()){
            Snackbar.make(view, "Details is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(text_date.text.isEmpty()){
            Snackbar.make(view, "Date is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(text_time.text.isEmpty()){
            Snackbar.make(view, "Time is Empty..", Snackbar.LENGTH_SHORT).show()
        }else{

            val title =edit_title.text.toString()

            val email =edit_email.text.toString()

           addTaskViewModel.updateTask(Task_response(updateId,numbertask,title,email,ItemPosition,formattedDate,formattedTime))

            Snackbar.make(view, "Update SuccessFull..", Snackbar.LENGTH_SHORT).show()
            edit_title.setText("")
            edit_email.setText("")
            text_date.text = ""
            text_time.text = ""
            progressDialog.dismiss()

            notificationShow("Update SuccessFull..")
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
                .setContentTitle("Update Screen")
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