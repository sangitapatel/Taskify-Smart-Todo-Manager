package com.taskify.todomanager.data.repository

import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun insertCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun updateCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun deleteCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun getAllCategories(): Flow<Resource<List<CategoryEntity>>>
    suspend fun insertDefaultCategoriesIfEmpty()
    suspend fun hasCategories(): Boolean
}