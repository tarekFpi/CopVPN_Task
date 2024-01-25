package com.example.mytodolist.view
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.mytodolist.R
import com.example.mytodolist.adapter.TaskListAdapter
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.utils.CheckInternetConnection
import com.example.mytodolist.utils.Resource
import com.example.mytodolist.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() , TaskListAdapter.EventDeleteIconClickInterface {

    private lateinit var taskListAdapter : TaskListAdapter;

    private lateinit var recyclerView_task: RecyclerView

    private lateinit var  progressDialog: ProgressDialog

    private lateinit var addTaskViewModel: TaskViewModel

    private var tasklList:  ArrayList<Task_response> = ArrayList()

    private lateinit var  editText_TaskSearch: EditText

    private lateinit var  lottieAnimationView: LottieAnimationView

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("please wait...")

        recyclerView_task = findViewById<RecyclerView>(R.id.recyclerView_task)

        editText_TaskSearch= findViewById(R.id.edit_taskSearch)

        lottieAnimationView= findViewById(R.id.animation_view)

        addTaskViewModel= ViewModelProvider(this)[TaskViewModel::class.java]

        editText_TaskSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable) {

                 searchTaskList(editable.toString())
            }
        })

    }




    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        taskListAdapter= TaskListAdapter(this,tasklList,this)

        recyclerView_task.apply {
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this@TaskListActivity)
            adapter=taskListAdapter

        }

        addTaskViewModel.postResponse.observe(this@TaskListActivity, Observer {

            when(it){
                is Resource.Loading -> {
                    progressDialog.show()

                }

                is Resource.Error -> {

                    apiError(it.message.toString())
                    progressDialog.dismiss()
                }

                is  Resource.Success -> {

                    if(it.data!!.isEmpty()){

                        lottieAnimationView.visibility=View.VISIBLE
                        progressDialog.dismiss()


                    }else{

                        lottieAnimationView.visibility=View.GONE
                        tasklList.clear()
                        tasklList.addAll(it.data!!)
                        taskListAdapter.setTaskList(tasklList)
                        taskListAdapter.notifyDataSetChanged()
                        progressDialog.dismiss()
                    }
                }
            }
        })


        internetConnection()
    }

    private  fun internetConnection(){

        if (!checkInternetConnection.isInternetAvailable(this))

            Toast.makeText(applicationContext, "No Internet", Toast.LENGTH_SHORT).show()


    }



    private fun apiError(it:String){
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun searchTaskList(textData: String) {
        try {
            val list =ArrayList<Task_response>()


     for (item: Task_response in tasklList) {

     if (item.title.toString().lowercase().contains(textData.toString().lowercase()))
       {
           list.add(item)
       }
      }

       taskListAdapter.filterdList(list)

        } catch (exception: Exception) {

            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun AddTaskBtn(view: View) {

        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }

    override fun onEventDeleteIconClick(event: Task_response) {
        event.id?.let {
           addTaskViewModel.deleteTask(it)

        }

        notificationShow("Task Delete SuccessFull..")

        Toast.makeText(this, "Task Delete SuccessFull..", Toast.LENGTH_SHORT).show()
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
                .setContentTitle("Delete")
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