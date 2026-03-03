package com.taskify.todomanager.ui.addTask

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.taskify.todomanager.R
import com.taskify.todomanager.ui.utils.parts.AppButton
import com.taskify.todomanager.ui.utils.parts.TaskForm
import com.taskify.todomanager.ui.utils.theme.MyTasksTheme
import com.taskify.todomanager.ui.utils.theme.primary
import com.taskify.todomanager.ui.utils.theme.white
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewTaskScreen(
    viewModel: NewTaskViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = primary,
            darkIcons = false
        )
    }

    val state by viewModel.state.collectAsState()
    val action = { action: NewTaskUiAction -> viewModel.doAction(action) }

    LaunchedEffect(Unit) {
        action(NewTaskUiAction.LoadCategories)
    }

    val isButtonEnabled =
        state.taskNameInput.isNotEmpty() &&
                state.taskDateInput.isNotEmpty() &&
                state.taskTimeInput.isNotEmpty()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            NewTaskTopBar(
                enabled = isButtonEnabled,
                onAddClick = {
                    action(NewTaskUiAction.AddTask)
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
                onNameValueChange = { action(NewTaskUiAction.UpdateTaskNameInput(it)) },
                taskDescValue = state.taskDescInput,
                onDescValueChange = { action(NewTaskUiAction.UpdateTaskDescInput(it)) },
                taskDateValue = state.taskDateInput,
                onDateValueChange = { action(NewTaskUiAction.UpdateTaskDateInput(it)) },
                taskTimeValue = state.taskTimeInput,
                onTimeValueChange = { action(NewTaskUiAction.UpdateTaskTimeInput(it)) },
                taskPriorityValue = state.taskPriorityInput,
                onPriorityValueChange = { action(NewTaskUiAction.UpdateTaskPriorityInput(it)) },
                taskCategoryValue = state.taskCategoryInput,
                onCategoryValueChange = { action(NewTaskUiAction.UpdateTaskCategoryInput(it)) },
                categoryData = state.categoryData.data ?: emptyList()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskTopBar(
    enabled: Boolean,
    onAddClick: () -> Unit,
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
                    text = stringResource(R.string.new_task),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = white
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = primary
            ),

            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = white
                    )
                }
            },

            // ✅ Icon Save Button
            /*  actions = {
                  IconButton(
                      onClick = onAddClick,
                      enabled = enabled
                  ) {
                      Icon(
                          imageVector = painterResource(R.drawable.done),
                          contentDescription = "Save Task",
                          tint = white,
                          modifier = Modifier.size(28.dp)
                      )

                  }
              }*/
            actions = {
                IconButton(
                    onClick = onAddClick,
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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun NewTaskScreenPreview() {
    MyTasksTheme {
        NewTaskScreen(
            navigateBack = {}
        )
    }
}
