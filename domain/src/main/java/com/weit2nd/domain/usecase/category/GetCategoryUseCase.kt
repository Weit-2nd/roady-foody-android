package com.weit2nd.domain.usecase.category

import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.repository.category.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository,
) {
    suspend fun invoke(): List<FoodCategory> {
        return repository.getCategories()
    }
}
