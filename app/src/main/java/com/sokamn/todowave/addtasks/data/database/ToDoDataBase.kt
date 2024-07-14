package com.sokamn.todowave.addtasks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1)
abstract class ToDoDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}