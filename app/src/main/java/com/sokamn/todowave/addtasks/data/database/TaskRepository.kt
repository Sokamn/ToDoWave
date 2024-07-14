package com.sokamn.todowave.addtasks.data.database

import com.sokamn.todowave.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val taskList: Flow<List<TaskModel>> = taskDao.getTasks().map { items -> items.map {
        TaskModel(id = it.id, name = it.name, selected = it.selected)
    } }

    suspend fun addTask(task: TaskModel) {
        taskDao.addTask(TaskEntity(id = task.id, name = task.name, selected = task.selected))
    }
}