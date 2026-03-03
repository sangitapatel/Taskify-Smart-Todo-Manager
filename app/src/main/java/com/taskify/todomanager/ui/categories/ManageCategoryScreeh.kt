package com.taskify.todomanager.ui.categories

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.taskify.todomanager.R
import com.taskify.todomanager.data.mechanism.Resource
import com.taskify.todomanager.ui.utils.appstate.error.EmptyView
import com.taskify.todomanager.ui.utils.appstate.error.ErrorView
import com.taskify.todomanager.ui.utils.appstate.loading.LoadingView
import com.taskify.todomanager.ui.utils.dialog.ConfirmDeleteDialog
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondaryWhite
import com.taskify.todomanager.ui.utils.theme.white
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageCategoryScreen(
    viewModel: ManageCategoryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor, darkIcons = false)
    }

    val state by viewModel.state.collectAsState()
    val action = { act: ManageCategoryUiAction -> viewModel.doAction(act) }

    LaunchedEffect(Unit) { action(ManageCategoryUiAction.LoadCategories) }

    Scaffold(
        containerColor = Color.White,
        topBar = { ManageCategoryTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { action(ManageCategoryUiAction.ShowAddDialog) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(1.dp),
                modifier = Modifier.size(55.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.plus), contentDescription = "Add", tint = Color.White, modifier = Modifier.size(25.dp))
            }
        },
        modifier = modifier
    ) { innerPadding ->

        when (state.categoryData) {
            is Resource.Error -> ErrorView(errorMessage = state.categoryData.message)
            is Resource.Success -> {
                val categoryList = state.categoryData.data ?: emptyList()
                if (categoryList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(categoryList) { category ->
                            CategoryItem(
                                data = category,
                                onEditClick = { action(ManageCategoryUiAction.ShowEditDialog(category)) },
                                onDeleteClick = { action(ManageCategoryUiAction.ShowDeleteDialog(category)) }
                            )
                        }
                    }
                } else {
                    EmptyView(text = stringResource(R.string.click_plus_to_add_category))
                }
            }
            else -> LoadingView()
        }
        // Add Category Dialog
        if (state.showAddDialog) {
            CategoryFormDialog(
                title = stringResource(R.string.add_category),
                value = state.categoryNameInput,
                selectedIconIndex = state.categoryIconIndex,
                iconList = CategoryIcons.iconList,
                onValueChange = { action(ManageCategoryUiAction.UpdateCategoryInput(it)) },
                onIconSelected = { action(ManageCategoryUiAction.UpdateCategoryIcon(it)) },
                onDismissRequest = { action(ManageCategoryUiAction.HideAddDialog) },
                onConfirmClick = { action(ManageCategoryUiAction.AddCategory) },
                isConfirmEnabled = state.categoryNameInput.isNotBlank() && state.categoryIconIndex >= 0
            )
        }

        // Edit Category Dialog
        if (state.showEditDialog) {
            CategoryFormDialog(
                title = stringResource(R.string.edit_category),
                value = state.editCategoryNameInput,
                selectedIconIndex = state.categoryIconIndex,
                iconList = CategoryIcons.iconList,
                onValueChange = { action(ManageCategoryUiAction.UpdateEditCategoryInput(it)) },
                onIconSelected = { action(ManageCategoryUiAction.UpdateCategoryIcon(it)) },
                onDismissRequest = { action(ManageCategoryUiAction.HideEditDialog) },
                onConfirmClick = { action(ManageCategoryUiAction.EditCategory) },
                isConfirmEnabled = state.editCategoryNameInput.isNotBlank()&& state.categoryIconIndex >= 0
            )
        }
        // Delete Confirmation Dialog
        if (state.showDeleteDialog) {
            ConfirmDeleteDialog(
                title = "Remove category?",
                text = "Are you sure you want to remove this category?",
                onConfirmClick = { action(ManageCategoryUiAction.DeleteCategory) },
                onDismissRequest = { action(ManageCategoryUiAction.HideDeleteDialog) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryTopBar() {
    Surface(
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = primary,
        tonalElevation = 4.dp
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "My Categories",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = white
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
    }
}


@Composable
fun CategoryFormDialog(
    title: String,
    value: String,
    selectedIconIndex: Int,
    iconList: List<ImageVector>,
    onValueChange: (String) -> Unit,
    onIconSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    isConfirmEnabled: Boolean
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(16.dp)) {
                Text(title, fontSize = 16.sp, color = Color.Black)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Category name") }
                )
                Spacer(Modifier.height(16.dp))
                Text("Choose Icon", fontWeight = FontWeight.Medium)

                LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.height(140.dp)) {
                    itemsIndexed(iconList) { index, icon ->
                        val isSelected = index == selectedIconIndex
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(56.dp)
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onIconSelected(index) }, // 🔹 pass index
                            contentAlignment = Alignment.Center
                        ) {
                            val icon2 = CategoryIcons.iconList.getOrElse(index) {
                                Icons.Default.List   // default fallback icon
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(Modifier.align(Alignment.End)) {
                    TextButton(onClick = onDismissRequest) { Text("Cancel") }
                    TextButton(enabled = isConfirmEnabled, onClick = onConfirmClick) { Text("Save") }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ManageCategoryPreview() {
    MyTasksTheme {
        ManageCategoryScreen()
    }
}
