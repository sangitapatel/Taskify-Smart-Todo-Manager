package com.taskify.todomanager.ui.maincontent

import kotlinx.serialization.Serializable

@Serializable
object HomeViewRoute

@Serializable
object AddTaskRoute

@Serializable
data class EditTaskRoute(val taskId: Int)

@Serializable
object ManageCategoryRoute
