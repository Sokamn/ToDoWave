package com.sokamn.todowave.addtasks.domain

import com.sokamn.todowave.addtasks.data.database.TaskRepository
import com.sokamn.todowave.addtasks.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel){
        taskRepository.deleteTask(taskModel)
    }
}