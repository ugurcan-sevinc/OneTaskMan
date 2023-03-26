package com.ugrcaan.onetaskman.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.ugrcaan.onetaskman.R
import com.ugrcaan.onetaskman.data.task.Task
import com.ugrcaan.onetaskman.data.task.TaskViewModel
import com.ugrcaan.onetaskman.databinding.RowTaskBinding

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private var taskList = emptyList<Task>()
    private lateinit var taskVM : TaskViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount() = taskList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(taskList: List<Task>){
        this.taskList = taskList
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: RowTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(task: Task) {
            taskVM = ViewModelProvider(binding.root.context as ViewModelStoreOwner)[TaskViewModel::class.java]
            binding.taskHeader.text = task.header
            binding.taskDescription.text = task.description
            binding.dateLabel.text = task.date
            isDoneCheck(task)

            binding.taskRow.setOnClickListener{
                task.isDone = setIsDone(task)
            }

            binding.categoryIndicator.setOnClickListener{
                task.isDone = setIsDone(task)
            }

            binding.taskRow.setOnLongClickListener {
                taskVM.deleteTask(task)
                notifyDataSetChanged()
                true
            }
        }

        private fun isDoneCheck(task: Task){
            if (task.isDone){
                binding.categoryIndicator.setBackgroundResource(R.drawable.bg_task_done)
                binding.categoryIndicator.setImageResource(R.drawable.ic_task_done)
            } else {
                binding.categoryIndicator.setBackgroundResource(R.drawable.bg_task_notdone)
                binding.categoryIndicator.setImageResource(0)
            }
        }

        private fun setIsDone(task: Task): Boolean{
            isDoneCheck(task)
            taskVM.updateTask(task)
            return !task.isDone
        }
    }


}