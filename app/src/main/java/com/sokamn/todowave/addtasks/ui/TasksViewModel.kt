package com.sokamn.todowave.addtasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokamn.todowave.addtasks.domain.AddTaskUseCase
import com.sokamn.todowave.addtasks.domain.DeleteTaskUseCase
import com.sokamn.todowave.addtasks.domain.GetTaskListUseCase
import com.sokamn.todowave.addtasks.domain.UpdateTaskUseCase
import com.sokamn.todowave.addtasks.ui.model.TaskModel
import com.sokamn.todowave.addtasks.ui.model.TasksUiState
import com.sokamn.todowave.addtasks.ui.model.TasksUiState.Error
import com.sokamn.todowave.addtasks.ui.model.TasksUiState.Loading
import com.sokamn.todowave.addtasks.ui.model.TasksUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTaskListUseCase
) : ViewModel() {

    val uiState: StateFlow<TasksUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showTaskDialog = MutableLiveData<Boolean>()
    val showTaskDialog: LiveData<Boolean> = _showTaskDialog

    private val _addTaskEnable = MutableLiveData<Boolean>()
    val addTaskEnable: LiveData<Boolean> = _addTaskEnable

    private val _task = MutableLiveData<String>()
    val task: LiveData<String> = _task

    fun onShowTaskDialog() {
        _showTaskDialog.value = true
    }

    fun onHideTaskDialog() {
        _showTaskDialog.value = false
    }

    fun onTaskChanged(text: String) {
        _addTaskEnable.value = text.isNotEmpty()
        _task.value = text
    }

    fun onCheckBoxTaskSelected(task: TaskModel) {
        viewModelScope.launch { updateTaskUseCase(task.copy(selected = !task.selected)) }
    }

    fun onAddTask(task: TaskModel) {
        _showTaskDialog.value = false
        onTaskChanged("")
        viewModelScope.launch { addTaskUseCase(task) }
    }

    fun onTaskDeleted(task: TaskModel) {
        viewModelScope.launch { deleteTaskUseCase(task) }
    }

}