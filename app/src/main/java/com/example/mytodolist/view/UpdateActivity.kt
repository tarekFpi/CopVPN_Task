package com.example.mytodolist.view
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
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
class UpdateActivity : AppCompatActivity() {

    private  lateinit var edit_title: EditText

    private  lateinit var edit_details: EditText

    private lateinit var  progressDialog: ProgressDialog

    private lateinit var addTaskViewModel: TaskViewModel

    private  lateinit var text_date: TextView

    private  lateinit var text_time: TextView

    private var updateId=0

    private lateinit var toolbar: Toolbar

    private  lateinit var  formattedDate:String

    private  lateinit var  formattedTime:String

    private val calendar = Calendar.getInstance()
    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        toolbar = findViewById(R.id.toolbar_updateActiviey)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val upArrow = resources.getDrawable(R.drawable.back)
        upArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
        supportActionBar!!.setHomeAsUpIndicator(upArrow)

        addTaskViewModel= ViewModelProvider(this)[TaskViewModel::class.java]

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("please wait...")

        edit_title =findViewById(R.id.edit_taskUpTitle)

        edit_details =findViewById(R.id.edit_taskUpDetails)

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

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                formattedTime =String.format("%d : %d", hourOfDay, minute)

                text_time.text =formattedDate
            }
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

        var bundle :Bundle ?=intent.extras

        var  title:String = bundle?.getString("title").toString()

        updateId  = bundle?.getInt("id")!!

        var  details:String = bundle.getString("details").toString()


        edit_title.setText(title)

        edit_details.setText(details)


    }

    fun update_button(view: View) {


        if(edit_title.text.isEmpty()){
            Snackbar.make(view, "Title is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(edit_details.text.isEmpty()){
            Snackbar.make(view, "Details is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(text_date.text.isEmpty()){
            Snackbar.make(view, "Date is Empty..", Snackbar.LENGTH_SHORT).show()

        }else if(text_time.text.isEmpty()){
            Snackbar.make(view, "Time is Empty..", Snackbar.LENGTH_SHORT).show()
        }else{

            var title =edit_title.text.toString()

            var details =edit_details.text.toString()

            addTaskViewModel.updateTask(Task_response(updateId,title,details,formattedDate,formattedTime))

            Snackbar.make(view, "Update SuccessFull..", Snackbar.LENGTH_SHORT).show()
            edit_title.setText("")
            edit_details.setText("")
            text_date.text = ""
            text_time.text = ""
            progressDialog.dismiss()
        }
    }
}