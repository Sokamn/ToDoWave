package com.sokamn.todowave.addtasks.domain

import com.sokamn.todowave.addtasks.data.database.TaskRepository
import com.sokamn.todowave.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(private val taskRepository: TaskRepository){
    operator fun invoke(): Flow<List<TaskModel>> = taskRepository.taskList
}