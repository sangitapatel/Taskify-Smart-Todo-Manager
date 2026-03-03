package com.taskify.todomanager.data.repository

import com.taskify.todomanager.data.entity.TaskEntity

interface ITaskReminderRepository {
    fun scheduleTaskReminder(task: TaskEntity)
    fun deleteTaskReminder(taskId: Int)
}