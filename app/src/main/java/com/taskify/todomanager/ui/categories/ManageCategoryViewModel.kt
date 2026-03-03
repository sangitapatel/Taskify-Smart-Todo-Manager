package com.taskify.todomanager.ui.categories

import androidx.lifecycle.viewModelScope
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.mechanism.Resource
import com.taskify.todomanager.data.repository.ICategoryRepository
import com.taskify.todomanager.ui.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageCategoryViewModel(
    private val categoryRepository: ICategoryRepository
) : BaseViewModel<ManageCategoryUiState, ManageCategoryUiAction>() {

    override val _state = MutableStateFlow(ManageCategoryUiState())

    /* ----------------------------------------------------- */
    /* ✅ DEFAULT CATEGORIES (iconRes = index same as yours)  */
    /* ----------------------------------------------------- */

    private val defaultCategories = listOf(
        CategoryEntity(name = "Work", iconRes = 0),
        CategoryEntity(name = "Home", iconRes = 1),
        CategoryEntity(name = "Personal", iconRes = 2)
    )

    /* ----------------------------------------------------- */
    /* -------------------- ACTIONS ------------------------- */
    /* ----------------------------------------------------- */

    override fun doAction(action: ManageCategoryUiAction) {
        when (action) {

            is ManageCategoryUiAction.UpdateCategoryInput ->
                _state.update { it.copy(categoryNameInput = action.newInput) }

            is ManageCategoryUiAction.UpdateEditCategoryInput ->
                _state.update { it.copy(editCategoryNameInput = action.newInput) }

            is ManageCategoryUiAction.UpdateCategoryIcon ->
                _state.update { it.copy(categoryIconIndex = action.icon) }

            is ManageCategoryUiAction.ShowEditDialog ->
                _state.update {
                    it.copy(
                        showEditDialog = true,
                        selectedCategory = action.category,
                        editCategoryNameInput = action.category.name,
                        categoryIconIndex = action.category.iconRes
                    )
                }

            is ManageCategoryUiAction.ShowDeleteDialog ->
                _state.update {
                    it.copy(
                        showDeleteDialog = true,
                        selectedCategory = action.category
                    )
                }

            ManageCategoryUiAction.AddCategory -> addCategory()
            ManageCategoryUiAction.EditCategory -> editCategory()
            ManageCategoryUiAction.DeleteCategory -> deleteCategory()
            ManageCategoryUiAction.LoadCategories -> loadCategories()

            ManageCategoryUiAction.ShowAddDialog ->
                _state.update {
                    it.copy(
                        showAddDialog = true,
                        categoryIconIndex = 0
                    )
                }

            ManageCategoryUiAction.HideAddDialog ->
                _state.update { it.copy(showAddDialog = false) }

            ManageCategoryUiAction.HideEditDialog ->
                _state.update { it.copy(showEditDialog = false) }

            ManageCategoryUiAction.HideDeleteDialog ->
                _state.update { it.copy(showDeleteDialog = false) }
        }
    }

    /* ----------------------------------------------------- */
    /* ✅ LOAD + AUTO INSERT DEFAULTS (ONLY IF EMPTY)         */
    /* ----------------------------------------------------- */

    private fun loadCategories() {
        viewModelScope.launch {

            categoryRepository.getAllCategories().collectLatest { resource ->

                // 🔥 Only first time when DB empty
                if (resource is Resource.Success && resource.data.isNullOrEmpty()) {

                    defaultCategories.forEach {
                        categoryRepository.insertCategory(it)
                    }

                    return@collectLatest
                }

                _state.update { state ->
                    state.copy(categoryData = resource)
                }
            }
        }
    }

    /* ----------------------------------------------------- */
    /* -------------------- ADD ----------------------------- */
    /* ----------------------------------------------------- */

    private fun addCategory() {
        viewModelScope.launch {

            val newCategory = CategoryEntity(
                name = state.value.categoryNameInput,
                iconRes = state.value.categoryIconIndex
            )

            categoryRepository.insertCategory(newCategory).collectLatest {
                _state.update { state ->
                    state.copy(
                        actionResult = it,
                        categoryNameInput = "",
                        showAddDialog = false
                    )
                }
            }

            loadCategories()
        }
    }

    /* ----------------------------------------------------- */
    /* -------------------- EDIT ---------------------------- */
    /* ----------------------------------------------------- */

    private fun editCategory() {
        viewModelScope.launch {

            val updated = state.value.selectedCategory?.copy(
                name = state.value.editCategoryNameInput,
                iconRes = state.value.categoryIconIndex
            )

            if (updated != null) {
                categoryRepository.updateCategory(updated).collectLatest {
                    _state.update { state ->
                        state.copy(
                            actionResult = it,
                            showEditDialog = false
                        )
                    }
                }
            }

            loadCategories()
        }
    }

    /* ----------------------------------------------------- */
    /* -------------------- DELETE -------------------------- */
    /* ----------------------------------------------------- */

    private fun deleteCategory() {
        viewModelScope.launch {

            state.value.selectedCategory?.let { category ->
                categoryRepository.deleteCategory(category).collectLatest {
                    _state.update { state ->
                        state.copy(
                            actionResult = it,
                            showDeleteDialog = false
                        )
                    }
                }
            }

            loadCategories()
        }
    }
}

/* ----------------------------------------------------- */
/* -------------------- UI STATE ------------------------ */
/* ----------------------------------------------------- */

data class ManageCategoryUiState(
    val actionResult: Resource<Unit> = Resource.Idle(),
    val selectedCategory: CategoryEntity? = null,

    val categoryNameInput: String = "",
    val editCategoryNameInput: String = "",

    val categoryIconIndex: Int = -1,

    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,

    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle()
)

/* ----------------------------------------------------- */
/* -------------------- UI ACTIONS ---------------------- */
/* ----------------------------------------------------- */

sealed class ManageCategoryUiAction {

    data class UpdateCategoryInput(val newInput: String) : ManageCategoryUiAction()
    data class UpdateEditCategoryInput(val newInput: String) : ManageCategoryUiAction()
    data class UpdateCategoryIcon(val icon: Int) : ManageCategoryUiAction()

    data class ShowEditDialog(val category: CategoryEntity) : ManageCategoryUiAction()
    data class ShowDeleteDialog(val category: CategoryEntity) : ManageCategoryUiAction()

    data object AddCategory : ManageCategoryUiAction()
    data object EditCategory : ManageCategoryUiAction()
    data object DeleteCategory : ManageCategoryUiAction()
    data object LoadCategories : ManageCategoryUiAction()

    data object ShowAddDialog : ManageCategoryUiAction()
    data object HideAddDialog : ManageCategoryUiAction()
    data object HideEditDialog : ManageCategoryUiAction()
    data object HideDeleteDialog : ManageCategoryUiAction()
}
