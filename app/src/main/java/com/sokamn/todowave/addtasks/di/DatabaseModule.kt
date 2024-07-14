package com.sokamn.todowave.addtasks.di

import android.content.Context
import androidx.room.Room
import com.sokamn.todowave.addtasks.data.database.TaskDao
import com.sokamn.todowave.addtasks.data.database.ToDoDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideTaskDao(toDoDataBase: ToDoDataBase): TaskDao{
        return toDoDataBase.taskDao()
    }

    @Provides
    @Singleton
    fun provideToDoDataBase(@ApplicationContext appContext: Context): ToDoDataBase {
        return Room.databaseBuilder(appContext, ToDoDataBase::class.java, "TaskDataBase").build()
    }
}