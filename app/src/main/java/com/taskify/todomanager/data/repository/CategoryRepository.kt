package com.taskify.todomanager.data.repository

import com.taskify.todomanager.data.dao.CategoryDao
import com.taskify.todomanager.data.entity.CategoryEntity
import com.taskify.todomanager.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepository(private val categoryDao: CategoryDao) : ICategoryRepository {
    override fun insertCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.insertCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to add category: ${e.message}"))
        }
    }

    override fun updateCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.updateCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to update category: ${e.message}"))
        }
    }

    override suspend fun hasCategories(): Boolean {
        return categoryDao.getCategoryCount() > 0
    }

    override suspend fun insertDefaultCategoriesIfEmpty() {
        if (!hasCategories()) {
            val defaultCategories = listOf(
                CategoryEntity(
                    name = "Work",
                    iconRes = 0
                ),
                CategoryEntity(
                    name = "Personal",
                    iconRes = 1
                ),
                CategoryEntity(
                    name = "Others",
                    iconRes = 0
                )
            )
            categoryDao.insertAll(defaultCategories)
        }
    }

    override fun deleteCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.deleteCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to delete category: ${e.message}"))
        }
    }

    override fun getAllCategories(): Flow<Resource<List<CategoryEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val result = categoryDao.getAllCategories()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch category: ${e.message}"))
        }
    }

}
