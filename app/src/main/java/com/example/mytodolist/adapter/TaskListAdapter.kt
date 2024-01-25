package com.example.mytodolist.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.databinding.TaskLayoutBinding
import com.example.mytodolist.model.task.Task_response
import com.example.mytodolist.view.UpdateActivity

class TaskListAdapter (private val context: Context,
                       private var taskList: ArrayList<Task_response>,
                       val eventDeleteIconClickInterface: EventDeleteIconClickInterface): RecyclerView.Adapter<TaskListAdapter.MyviewHolder>() {

    private var listposition = -1

        class MyviewHolder(val binding: TaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

       }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {

        val binding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyviewHolder(binding)

    }

    fun setTaskList(list: ArrayList<Task_response>) {

        this.taskList =list
        notifyDataSetChanged()
    }

    fun filterdList(filterList: ArrayList<Task_response>) {

        taskList = filterList

        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = taskList.size


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {

        val itemPosition = taskList[position]

        holder.binding.textTaskNumber.text ="number:"+ itemPosition.number

        holder.binding.textTaskTitle.text ="text:"+ itemPosition.title

        holder.binding.textTaskEmail.text= "email:"+itemPosition.email

        holder.binding.textTaskDate.text="date:"+ itemPosition.formattedDate

        holder.binding.textTaskTime.text="time:"+ itemPosition.formattedTime


        holder.binding.taskUpdteIcons.setOnClickListener {

            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("id", itemPosition.id)
            intent.putExtra("text", itemPosition.title)
            intent.putExtra("email",itemPosition.email)
            intent.putExtra("number",itemPosition.number)
            intent.putExtra("item",itemPosition.selectItem)

             context.startActivity(intent)

        }


        holder.binding.checkboxDelete.setOnClickListener {

            if(holder.binding.checkboxDelete.isChecked){

                eventDeleteIconClickInterface?.onEventDeleteIconClick(taskList.get(position))
                taskList.remove(itemPosition)
                notifyDataSetChanged()
                holder.binding.checkboxDelete.isChecked =false
            }
        }


        setAnimation(holder.itemView, position)
    }

    interface EventDeleteIconClickInterface {
        fun onEventDeleteIconClick(event: Task_response)
    }

    private fun setAnimation(viewAnimition: View, position: Int) {
        if (position > listposition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            animation.duration = 1000
            viewAnimition.startAnimation(animation)
            listposition = position
        }
    }
}