package com.example.mytodolist.view
import android.annotation.SuppressLint
import android.app.*
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mytodolist.R
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    private  lateinit var edit_title: EditText

    private  lateinit var edit_details: EditText

    private  lateinit var text_date: TextView

    private  lateinit var text_time: TextView

    private lateinit var addTaskViewModel: TaskViewModel

    private lateinit var toolbar: Toolbar

    private  lateinit var  formattedDate:String

    private  lateinit var  formattedTime:String

    private val calendar = Calendar.getInstance()
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

        addTaskViewModel= ViewModelProvider(this)[TaskViewModel::class.java]

        edit_title =findViewById(R.id.edit_taskTitle)

        edit_details =findViewById(R.id.edit_taskDetails)

        text_date =findViewById(R.id.add_taskDate)

        text_time =findViewById(R.id.add_taskTime)


        text_time.setOnClickListener {

            showTimePicker()
        }

        text_date.setOnClickListener {

        showDatePicker()

        }

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

        if(edit_title.text.isEmpty()){
            Snackbar.make(view, "Title is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(edit_details.text.isEmpty()){
            Snackbar.make(view, "Details is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(text_date.text.isEmpty()){
            Snackbar.make(view, "Date is Empty..", Snackbar.LENGTH_SHORT).show()
        }else{


             var title =edit_title.text.toString()

             var details =edit_details.text.toString()

              var taskResponse = Task_response(null,title,details,formattedDate,formattedTime)
              addTaskViewModel.addTask(taskResponse)

            edit_title.setText("")
            edit_details.setText("")
            text_date.text = ""
            text_time.text = ""
            Toast.makeText(applicationContext, "Save SuccessFull..", Toast.LENGTH_SHORT).show()


        }

    }


}