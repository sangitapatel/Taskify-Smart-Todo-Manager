package com.taskify.todomanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [(Index(value = ["category_id"]))]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "priority") val priority: Int = 2, // 👈 NEW (1 High, 2 Medium, 3 Low)
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
    @ColumnInfo(name = "is_pinned") var isPinned: Boolean = false,
    @ColumnInfo(name = "category_id") val categoryId: Int?

)

enum class TaskPriority(val value: Int) {
    HIGH(1),
    MEDIUM(2),
    LOW(3)
}