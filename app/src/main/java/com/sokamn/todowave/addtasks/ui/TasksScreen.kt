package com.sokamn.todowave.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sokamn.todowave.addtasks.ui.model.TaskModel

@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {
    val showTaskDialog: Boolean by tasksViewModel.showTaskDialog.observeAsState(initial = false)
    val addTaskEnable: Boolean by tasksViewModel.addTaskEnable.observeAsState(initial = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AddTasksDialog(show = showTaskDialog,
            addTaskEnable,
            tasksViewModel = tasksViewModel,
            onDismiss = {
                tasksViewModel.onHideTaskDialog()
            },
            onTaskAdded = { task ->
                tasksViewModel.onAddTask(TaskModel(name = task))
            })

        TasksList(tasksViewModel)

        FabDialog(modifier = Modifier.align(Alignment.BottomEnd)) {
            tasksViewModel.onShowTaskDialog()
        }
    }
}

@Composable
fun TasksList(tasksViewModel: TasksViewModel) {
    val myTasks by tasksViewModel.taskList.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = myTasks, key = { it.id }) { task ->
            ItemTask(task = task, onCheckBoxTaskSelected = { taskSelected ->
                tasksViewModel.onCheckBoxTaskSelected(taskSelected)
            }, onTaskDeleted = { taskDeleted ->
                tasksViewModel.onTaskDeleted(taskDeleted)
            })
        }
    }
}

@Composable
fun ItemTask(
    task: TaskModel,
    onCheckBoxTaskSelected: (TaskModel) -> Unit,
    onTaskDeleted: (TaskModel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    onTaskDeleted(task)
                })
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(modifier = Modifier.padding(start = 8.dp), text = task.name)
            Checkbox(checked = task.selected, onCheckedChange = { onCheckBoxTaskSelected(task) })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(modifier = modifier.padding(16.dp),
        onClick = {
            onClick()
        }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}

@Composable
fun AddTasksDialog(
    show: Boolean,
    addTaskEnable: Boolean,
    tasksViewModel: TasksViewModel,
    onDismiss: () -> Unit,
    onTaskAdded: (String) -> Unit
) {
    val task by tasksViewModel.task.observeAsState("")
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Añade tu tarea", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = task,
                    onValueChange = { tasksViewModel.onTaskChanged(text = it) },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                tasksViewModel.onTaskChanged(text = "")
                            },
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = addTaskEnable,
                    onClick = { onTaskAdded(task) }) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}