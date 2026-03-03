package com.taskify.todomanager.ui.home.parts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.entity.TaskEntity


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(
    tasks: List<TaskEntity>,
    categories: List<CategoryEntity>,
    onEditTask: (TaskEntity) -> Unit,
    onLongPress: (Boolean) -> Unit,
    onPinClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    var isMultiSelectMode by remember { mutableStateOf(false) }
    var selectedTasks by remember { mutableStateOf(setOf<Int>()) }

    LazyColumn(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(tasks) { task ->
            val categoryName = categories.find { it.id == task.categoryId }?.name

            TaskCard(
                dataTask = task,
                categoryName = categoryName,
                isSelected = selectedTasks.contains(task.id),
                onSelect = {
                    if (isMultiSelectMode) {
                        selectedTasks = if (selectedTasks.contains(task.id)) {
                            selectedTasks - task.id
                        } else {
                            selectedTasks + task.id
                        }

                        if (selectedTasks.isEmpty()) {
                            isMultiSelectMode = false
                            onLongPress(false)
                        }
                    } else {
                        onEditTask(task)
                    }
                },
                onLongPress = {
                    isMultiSelectMode = true
                    selectedTasks = selectedTasks + task.id
                    onLongPress(isMultiSelectMode)
                },
                onPinClick = { onPinClick() },
                onDoneClick = { onDoneClick() }
            )

        }
    }
}


