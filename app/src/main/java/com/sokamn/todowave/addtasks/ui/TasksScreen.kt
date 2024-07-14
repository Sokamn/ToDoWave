package com.sokamn.todowave.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun TasksScreen() {
    var showTaskDialog by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        AddTasksDialog(show = showTaskDialog, onDismiss = {
            showTaskDialog = false
        },
            onTaskAdded = { task ->

            })
        FabDialog(modifier = Modifier.align(Alignment.BottomEnd)) {
            showTaskDialog = true
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
fun AddTasksDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember { mutableStateOf("") }
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
                    value = myTask,
                    onValueChange = { myTask = it },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable {
                                myTask = ""
                            },
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = { onTaskAdded(myTask) }) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}