package com.sokamn.todowave.addtasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokamn.todowave.addtasks.domain.AddTaskUseCase
import com.sokamn.todowave.addtasks.domain.GetTaskListUseCase
import com.sokamn.todowave.addtasks.ui.model.TaskModel
import com.sokamn.todowave.addtasks.ui.model.TasksUiState
import com.sokamn.todowave.addtasks.ui.model.TasksUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    getTasksUseCase: GetTaskListUseCase
) : ViewModel() {

    val uiState: StateFlow<TasksUiState> = getTasksUseCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showTaskDialog = MutableLiveData<Boolean>()
    val showTaskDialog: LiveData<Boolean> = _showTaskDialog

    private val _addTaskEnable = MutableLiveData<Boolean>()
    val addTaskEnable: LiveData<Boolean> = _addTaskEnable

    private val _taskList = MutableStateFlow<List<TaskModel>>(emptyList())
    val taskList: StateFlow<List<TaskModel>> = _taskList

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
        val index = _taskList.value.indexOf(task)
        val updatedTaskList = _taskList.value.toMutableList()
        updatedTaskList[index] = updatedTaskList[index].let { taskSelected ->
            taskSelected.copy(selected = !taskSelected.selected)
        }
        _taskList.value = updatedTaskList
    }

    fun onAddTask(task: TaskModel) {
        _taskList.value += task
        _showTaskDialog.value = false
        onTaskChanged("")

        viewModelScope.launch {
            addTaskUseCase(task)
        }
    }

    fun onTaskDeleted(task: TaskModel) {
        val taskDeleted = _taskList.value.find {
            it.id == task.id
        }
        _taskList.value -= taskDeleted!!
    }

}