package com.sokamn.todowave.addtasks.ui.model

data class TaskModel(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    var selected: Boolean = false
)