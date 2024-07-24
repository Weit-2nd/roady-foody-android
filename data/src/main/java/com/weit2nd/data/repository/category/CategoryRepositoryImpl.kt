package com.weit2nd.data.repository.category

import com.weit2nd.data.model.category.CategoryDTO
import com.weit2nd.data.source.category.CategoryCacheDataSource
import com.weit2nd.data.source.category.CategoryDataSource
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.repository.category.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val categoryCacheDataSource: CategoryCacheDataSource,
) : CategoryRepository {
    override suspend fun getCategories(): List<FoodSpotCategory> {
        if (categoryCacheDataSource.isCategoriesNotExist()) {
            val categories = categoryDataSource.getCategories().map { it.toFoodSpotCategory() }
            categoryCacheDataSource.setCategories(categories)
        }
        return categoryCacheDataSource.getCategories()
    }

    private fun CategoryDTO.toFoodSpotCategory() =
        FoodSpotCategory(
            id = id,
            name = name,
        )
}
