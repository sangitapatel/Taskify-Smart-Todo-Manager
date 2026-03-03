package com.taskify.todomanager.di

import com.taskify.todomanager.ui.Edit.EditTaskViewModel
import com.taskify.todomanager.ui.addTask.NewTaskViewModel

import com.taskify.todomanager.ui.home.HomeViewModel
import com.taskify.todomanager.ui.categories.ManageCategoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel{ HomeViewModel(get(), get()) }
    viewModel{ NewTaskViewModel(get(), get()) }
    viewModel { ManageCategoryViewModel(get()) }
    viewModel { EditTaskViewModel(get(), get()) }
}