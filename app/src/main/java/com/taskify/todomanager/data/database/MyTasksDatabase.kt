package com.taskify.todomanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.taskify.todomanager.data.dao.CategoryDao
import com.taskify.todomanager.data.dao.TaskDao
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.entity.TaskEntity

@Database(entities = [CategoryEntity::class, TaskEntity::class], version = 5, exportSchema = true)

abstract class MyTasksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}
