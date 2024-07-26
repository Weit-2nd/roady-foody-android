package com.weit2nd.data.repository.category

import com.weit2nd.data.model.category.FoodCategoryDTO
import com.weit2nd.data.source.category.CategoryCacheDataSource
import com.weit2nd.data.source.category.CategoryDataSource
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.repository.category.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val categoryCacheDataSource: CategoryCacheDataSource,
) : CategoryRepository {
    override suspend fun getCategories(): List<FoodCategory> {
        if (categoryCacheDataSource.isCategoriesNotExist()) {
            val categories = categoryDataSource.getCategories().map { it.toFoodCategory() }
            categoryCacheDataSource.setCategories(categories)
        }
        return categoryCacheDataSource.getCategories()
    }

    private fun FoodCategoryDTO.toFoodCategory() =
        FoodCategory(
            id = id,
            name = name,
        )
}
