package com.taskify.todomanager.ui.addTask

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.entity.TaskEntity
import com.taskify.todomanager.data.mechanism.Resource
import com.taskify.todomanager.data.repository.ICategoryRepository
import com.taskify.todomanager.data.repository.ITaskRepository
import com.taskify.todomanager.ui.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewTaskViewModel(
    val taskRepository: ITaskRepository,
    val categoryRepository: ICategoryRepository,
) : BaseViewModel<NewTaskUiState, NewTaskUiAction>() {
    override val _state = MutableStateFlow(NewTaskUiState())
    var selectedPriority by mutableStateOf(2)

    override fun doAction(action: NewTaskUiAction) {
        when (action) {
            is NewTaskUiAction.UpdateTaskNameInput -> _state.update { it.copy(taskNameInput = action.newInput) }
            is NewTaskUiAction.UpdateTaskDescInput -> _state.update { it.copy(taskDescInput = action.newDesc) }
            is NewTaskUiAction.UpdateTaskDateInput -> _state.update { it.copy(taskDateInput = action.newDate) }
            is NewTaskUiAction.UpdateTaskTimeInput -> _state.update { it.copy(taskTimeInput = action.newTime) }
            is NewTaskUiAction.UpdateTaskPriorityInput -> _state.update { it.copy(taskPriorityInput = action.newPriority) }
            is NewTaskUiAction.UpdateTaskCategoryInput -> _state.update { it.copy(taskCategoryInput = action.newCategory) }
            is NewTaskUiAction.LoadCategories -> loadCategories()
            NewTaskUiAction.AddTask -> addTaskData()
        }
    }

    private fun addTaskData() {
        viewModelScope.launch {
            taskRepository.insertTask(
                TaskEntity(
                    name = state.value.taskNameInput,
                    desc = state.value.taskDescInput,
                    date = state.value.taskDateInput,
                    time = state.value.taskTimeInput,
                    priority = state.value.taskPriorityInput,
                    categoryId = if(state.value.taskCategoryInput.isEmpty()) null else state.value.taskCategoryInput.toInt(),
                )
            ).collectLatest { result ->
                _state.update { state ->
                    state.copy(
                        result = result
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collectLatest { categories ->
                _state.update { state ->
                    state.copy(
                        categoryData = categories
                    )
                }
            }
        }
    }
}

data class NewTaskUiState(
    val taskNameInput: String = "",
    val taskDescInput: String = "",
    val taskDateInput: String = "",
    val taskTimeInput: String = "",
    val taskPriorityInput: Int = 2,
    val taskCategoryInput: String = "",
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle(),
    val result: Resource<Unit> = Resource.Idle()
)

sealed class NewTaskUiAction {
    data class UpdateTaskNameInput(val newInput: String) : NewTaskUiAction()
    data class UpdateTaskDescInput(val newDesc: String) : NewTaskUiAction()
    data class UpdateTaskDateInput(val newDate: String) : NewTaskUiAction()
    data class UpdateTaskTimeInput(val newTime: String) : NewTaskUiAction()
    data class UpdateTaskPriorityInput(val newPriority: Int) : NewTaskUiAction()
    data class UpdateTaskCategoryInput(val newCategory: String) : NewTaskUiAction()
    data object AddTask: NewTaskUiAction()
    data object LoadCategories: NewTaskUiAction()
}
