package com.taskify.todomanager.ui.maincontent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.ui.Edit.EditTaskScreen
import com.taskify.todomanager.ui.addTask.NewTaskScreen
import com.taskify.todomanager.ui.home.HomeScreen
import com.taskify.todomanager.ui.categories.ManageCategoryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
     val defaultCategories = listOf(
        CategoryEntity(name = "Work", iconRes = 0),
        CategoryEntity(name = "Home", iconRes = 1),
        CategoryEntity(name = "Personal", iconRes = 2)
    )
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val selectedTab = when {
        backStackEntry?.destination?.hasRoute(HomeViewRoute::class) == true ->
            BottomTab.HOME

        backStackEntry?.destination?.hasRoute(AddTaskRoute::class) == true ->
            BottomTab.ADD

        backStackEntry?.destination?.hasRoute(ManageCategoryRoute::class) == true ->
            BottomTab.CATEGORY

        else -> BottomTab.HOME
    }

    Scaffold(
        modifier = modifier,
//        contentColor = primary,
        bottomBar = {
            BottomChipBar(

                selectedTab = selectedTab,
                onHomeClick = {
                    navController.navigate(HomeViewRoute) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onAddClick = {
                    navController.navigate(AddTaskRoute)
                },
                onCategoryClick = {
                    navController.navigate(ManageCategoryRoute) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeViewRoute,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<HomeViewRoute> {
                HomeScreen(
                    onEditTask = { taskId ->
                        navController.navigate(EditTaskRoute(taskId))
                    }
                )
            }

            composable<AddTaskRoute> {
                NewTaskScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }

            composable<ManageCategoryRoute> {
                ManageCategoryScreen()
            }

            composable<EditTaskRoute> { backStackEntry ->
                val taskId = backStackEntry.toRoute<EditTaskRoute>().taskId
                EditTaskScreen(
                    taskId = taskId,
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

