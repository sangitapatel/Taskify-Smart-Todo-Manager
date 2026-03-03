package com.taskify.todomanager.data.repository

import android.content.Context
import com.taskify.todomanager.data.entity.TaskEntity
import com.taskify.todomanager.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun insertTask(task: TaskEntity): Flow<Resource<Unit>>
    fun updateTask(task: TaskEntity): Flow<Resource<Unit>>
    fun deleteTask(task: TaskEntity): Flow<Resource<Unit>>
    fun deleteTasks(taskIds: List<Int>): Flow<Resource<Unit>>
    fun getAllTasks(): Flow<Resource<List<TaskEntity>>>
    fun searchTasks(query: String): Flow<Resource<List<TaskEntity>>>
    fun filterTasksByCategory(categoryId: Int): Flow<Resource<List<TaskEntity>>>
    fun pinTask(taskId: Int, isPinned: Boolean): Flow<Resource<Unit>>
    fun markAsDone(taskId: Int, isDone: Boolean): Flow<Resource<Unit>>
    fun multipleMarkAsDone(taskIds: List<Int>): Flow<Resource<Unit>>
    fun getTaskById(taskId: Int): Flow<Resource<TaskEntity>>
    fun getPriorityTasks(): Flow<Resource<List<TaskEntity>>>
}
