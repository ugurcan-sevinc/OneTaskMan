package com.ugrcaan.onetaskman.data.task

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    val readAllTasks: LiveData<List<Task>> = taskDao.readAllTasks()

    val readDoneTasks: LiveData<List<Task>> = taskDao.readDoneTasks()

    suspend fun addTask(task: Task){
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks(){
        taskDao.deleteAllTasks()
    }
}
