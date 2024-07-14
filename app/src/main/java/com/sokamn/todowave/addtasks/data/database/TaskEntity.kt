package com.sokamn.todowave.addtasks.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    var selected: Boolean = false
)