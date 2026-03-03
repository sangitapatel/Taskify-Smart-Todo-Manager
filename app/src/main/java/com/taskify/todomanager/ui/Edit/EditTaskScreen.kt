package com.taskify.todomanager.ui.Edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.taskify.todomanager.R
import com.taskify.todomanager.ui.utils.parts.TaskForm
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.white
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTaskScreen(
    taskId: Int,
    navigateBack: () -> Unit,
    viewModel: EditTaskViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // blue background → white icons
        )
    }
    val state by viewModel.state.collectAsState()
    val action = { action: EditTaskUiAction -> viewModel.doAction(action) }
    LaunchedEffect(Unit) {
        action(EditTaskUiAction.GetAllCategories)
        action(EditTaskUiAction.SetCurrentData(taskId))
    }

    val isButtonEnabled =
        state.taskNameInput.isNotEmpty() &&
                state.taskDateInput.isNotEmpty() &&
                state.taskTimeInput.isNotEmpty()

    Scaffold(
        topBar = {
            EditTaskTopBar(
                enabled = isButtonEnabled,
                onDoneClick = {
                    action(EditTaskUiAction.EditTask)
                    navigateBack()
                },
                navigateBack = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TaskForm(
                taskNameValue = state.taskNameInput,
                onNameValueChange = { action(EditTaskUiAction.UpdateTaskNameInput(it)) },
                taskDescValue = state.taskDescInput,
                onDescValueChange = { action(EditTaskUiAction.UpdateTaskDescInput(it)) },
                taskDateValue = state.taskDateInput,
                onDateValueChange = { action(EditTaskUiAction.UpdateTaskDateInput(it)) },
                taskTimeValue = state.taskTimeInput,
                onTimeValueChange = { action(EditTaskUiAction.UpdateTaskTimeInput(it)) },
                taskPriorityValue = state.taskPriorityInput,
                onPriorityValueChange = { action(EditTaskUiAction.UpdateTaskPriorityInput(it)) },
                taskCategoryValue = state.taskCategoryInput,
                onCategoryValueChange = { action(EditTaskUiAction.UpdateTaskCategoryInput(it)) },
                categoryData = state.categoryData.data ?: emptyList()
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskTopBar(
    enabled: Boolean,
    onDoneClick: () -> Unit,
    navigateBack: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = primary,
        tonalElevation = 4.dp
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.edit_task),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = white
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = primary
            ),
            navigationIcon = {
                IconButton(onClick = { navigateBack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = white
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onDoneClick,
                    enabled = enabled
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.done),
                        contentDescription = "Save Task",
                        tint = white,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        )
    }
}
