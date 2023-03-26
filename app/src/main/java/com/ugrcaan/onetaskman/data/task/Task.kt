package com.ugrcaan.onetaskman.data.task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val header: String,
    val description: String,
    val date: String,
    var isDone: Boolean
    )