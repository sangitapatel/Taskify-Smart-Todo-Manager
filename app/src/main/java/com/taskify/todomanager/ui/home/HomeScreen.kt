package com.taskify.todomanager.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.entity.TaskEntity
import com.taskify.todomanager.data.mechanism.Resource
import com.taskify.todomanager.ui.home.parts.*
import com.taskify.todomanager.ui.home.parts.topbar.*
import com.taskify.todomanager.ui.home.util.categorizeTasks
import com.taskify.todomanager.ui.utils.appstate.error.EmptyView
import com.taskify.todomanager.ui.utils.appstate.error.ErrorView
import com.taskify.todomanager.ui.utils.appstate.loading.LoadingView
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.secondary2
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),   // ⭐ preview safe
    onEditTask: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val vm = viewModel ?: koinViewModel()   // ⭐ real vm if null

    val state by vm.state.collectAsState()
    val action = { a: HomeUiAction -> vm.doAction(a) }

    val tasks = state.taskData.data
    val categories = state.categoryData.data ?: emptyList()
    val groupedTasks = tasks?.let { categorizeTasks(it) }

    val sectionStates = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(Unit) {
        vm.initDefaultCategories()
        action(HomeUiAction.GetAllTasks)
        action(HomeUiAction.GetAllCategories)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {

            when {
                state.isSelectMode -> {
                    MultipleSelectTopBar(
                        navigateBack = { action(HomeUiAction.ExitSelectMode) },
                        onDoneClick = { action(HomeUiAction.MarkAsDoneMultiple) },
                        onDeleteClick = { action(HomeUiAction.DeleteSelectedTasks) },
                        totalSelectedTasks = state.multipleSelectedTasks.size.toString()
                    )
                }

                state.isSearchMode -> {
                    SearchTopBar(
                        navigateBack = { action(HomeUiAction.ExitSearchMode) },
                        onValueChange = {
                            action(HomeUiAction.SearchInputing(it))
                            action(HomeUiAction.SearchTasks)
                        },
                        value = state.searchInput
                    )
                }

                else -> AppTopBar(
                    onSearchClick = { action(HomeUiAction.EnterSearchMode) }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            CategoryButtonList(
                categories = categories,
                selectedCategoryId = state.selectedCategory,
                onCategorySelected = { id ->
                    action(HomeUiAction.SelectCategoryFilter(id))

                    if (id == null)
                        action(HomeUiAction.GetAllTasks)
                    else
                        action(HomeUiAction.GetFilteredTasks(id))
                }
            )

            when (state.taskData) {

                is Resource.Loading -> LoadingView()

                is Resource.Error ->
                    ErrorView(errorMessage = state.taskData.message)

                is Resource.Success -> {

                    if (tasks.isNullOrEmpty()) {
                        EmptyView()
                        return@Column
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {

                        groupedTasks?.forEach { (title, taskList) ->

                            if (taskList.isEmpty()) return@forEach

                            val expanded = sectionStates.getOrPut(title) { true }

                            item {
                                SectionHeader(
                                    title = title,
                                    count = taskList.size,
                                    expanded = expanded,
                                    onToggle = {
                                        sectionStates[title] = !expanded
                                    }
                                )
                            }

                            if (expanded) {

                                itemsIndexed(taskList) { index, task ->

                                    val categoryName =
                                        categories.find { it.id == task.categoryId }?.name

                                    val isSelected =
                                        state.multipleSelectedTasks.contains(task.id)

                                    TaskCard(
                                        dataTask = task,
                                        categoryName = categoryName,
                                        isSelected = isSelected,
                                        onSelect = {
                                            if (state.isSelectMode)
                                                action(HomeUiAction.SelectTask(task.id))
                                            else
                                                onEditTask(task.id)
                                        },
                                        onLongPress = {
                                            action(HomeUiAction.EnterSelectMode(task.id))
                                        },
                                        onPinClick = {
                                            action(HomeUiAction.PinTask(task.id))
                                        },
                                        onDoneClick = {
                                            action(HomeUiAction.MarkAsDone(task.id))
                                        }
                                    )

                                    if (index < taskList.lastIndex) {
                                        HorizontalDivider(
                                            thickness = 0.6.dp,
                                            color = Color(0xFFE9E9E9)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit
) {

    Surface(
        color = secondary2.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() }
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                color = primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = count.toString(),
                color = primary
            )

            Spacer(Modifier.width(6.dp))

            Icon(
                imageVector =
                    if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = primary
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {

    val dummyTasks = listOf(
        TaskEntity(1,"Buy Groceries","","08 - 02 - 2026","15:30",2,false,true,1),
        TaskEntity(2,"Workout","","09 - 02 - 2026","18:00",1,false,false,2)
    )

    val dummyCategories = listOf(
        CategoryEntity(1,"Work",0),
        CategoryEntity(2,"Personal",0)
    )

    val grouped = categorizeTasks(dummyTasks)

    MaterialTheme {

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {

            grouped.forEach { (title, taskList) ->

                var expanded by remember { mutableStateOf(true) }

                SectionHeader(
                    title = title,
                    count = taskList.size,
                    expanded = expanded,
                    onToggle = { expanded = !expanded }
                )

                if (expanded) {
                    taskList.forEach { task ->
                        TaskCard(
                            dataTask = task,
                            categoryName =
                                dummyCategories.find { it.id == task.categoryId }?.name,
                            onSelect = {},     // preview only
                            onLongPress = {},
                            onPinClick = {},
                            onDoneClick = {},
                            isSelected = false
                        )
                    }
                }
            }
        }
    }
}