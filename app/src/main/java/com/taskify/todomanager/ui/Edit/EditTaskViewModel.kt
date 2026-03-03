package com.taskify.todomanager.ui.Edit
import androidx.lifecycle.viewModelScope
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.entity.TaskEntity
import com.taskify.todomanager.data.mechanism.Resource
import com.taskify.todomanager.data.repository.ICategoryRepository
import com.taskify.todomanager.data.repository.ITaskRepository
import com.taskify.todomanager.ui.addTask.NewTaskUiAction
import com.taskify.todomanager.ui.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTaskViewModel(
    val taskRepository: ITaskRepository,
    val categoryRepository: ICategoryRepository
) : BaseViewModel<EditTaskUiState, EditTaskUiAction>() {
    override val _state = MutableStateFlow(EditTaskUiState())

    override fun doAction(action: EditTaskUiAction) {
        when (action) {
            is EditTaskUiAction.UpdateTaskNameInput -> _state.update { it.copy(taskNameInput = action.newInput) }
            is EditTaskUiAction.UpdateTaskDescInput -> _state.update { it.copy(taskDescInput = action.newDesc) }
            is EditTaskUiAction.UpdateTaskDateInput -> _state.update { it.copy(taskDateInput = action.newDate) }
            is EditTaskUiAction.UpdateTaskTimeInput -> _state.update { it.copy(taskTimeInput = action.newTime) }
            is EditTaskUiAction.UpdateTaskPriorityInput -> _state.update { it.copy(taskPriorityInput = action.newPriority) }
            is EditTaskUiAction.UpdateTaskCategoryInput -> _state.update { it.copy(taskCategoryInput = action.newCategory) }
            is EditTaskUiAction.SetCurrentData -> getCurrentTask(action.currentDataId)
            EditTaskUiAction.EditTask -> editTaskData()
            EditTaskUiAction.GetAllCategories -> loadCategories()
        }
    }

    private fun getCurrentTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId).collectLatest { data ->
                _state.update {
                    it.copy(
                        currentData = data
                    )
                }
            }

            setAllDefaultInput()
        }
    }

    private fun setAllDefaultInput() {
        val currentData = state.value.currentData.data
        if (currentData != null) {
            _state.update {
                it.copy(
                    taskNameInput = currentData.name,
                    taskDateInput = currentData.date,
                    taskTimeInput = currentData.time,
                    taskCategoryInput = currentData.categoryId.toString(),
                )
            }
        }
    }

    private fun editTaskData() {
        viewModelScope.launch {
            val newTaskEdit = state.value.currentData.data?.copy(
                name = state.value.taskNameInput,
                date = state.value.taskDateInput,
                time = state.value.taskTimeInput,
                categoryId = state.value.taskCategoryInput.toIntOrNull()
            )
            if (newTaskEdit != null) {
                taskRepository.updateTask(
                    newTaskEdit
                ).collectLatest { result ->
                    _state.update { state ->
                        state.copy(
                            result = result
                        )
                    }
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories(
            ).collectLatest { categories ->
                _state.update { state ->
                    state.copy(
                        categoryData = categories
                    )
                }
            }
        }
    }
}

data class EditTaskUiState(
    val taskNameInput: String = "",
    val taskDescInput: String = "",
    val taskDateInput: String = "",
    val taskTimeInput: String = "",
    val taskPriorityInput: Int = 2,
    val taskCategoryInput: String = "",
    val currentData: Resource<TaskEntity> = Resource.Idle(),
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle(),
    val result: Resource<Unit> = Resource.Idle()
)

sealed class EditTaskUiAction {
    data class UpdateTaskNameInput(val newInput: String) : EditTaskUiAction()
    data class UpdateTaskDescInput(val newDesc: String) : EditTaskUiAction()
    data class UpdateTaskDateInput(val newDate: String) : EditTaskUiAction()
    data class UpdateTaskTimeInput(val newTime: String) : EditTaskUiAction()
    data class UpdateTaskPriorityInput(val newPriority: Int) : EditTaskUiAction()
    data class UpdateTaskCategoryInput(val newCategory: String) : EditTaskUiAction()
    data object EditTask : EditTaskUiAction()
    data class SetCurrentData(val currentDataId: Int) : EditTaskUiAction()
    data object GetAllCategories : EditTaskUiAction()
}