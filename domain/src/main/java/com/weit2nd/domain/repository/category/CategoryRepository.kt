package com.weit2nd.domain.repository.category

import com.weit2nd.domain.model.spot.FoodCategory

interface CategoryRepository {
    suspend fun getCategories(): List<FoodCategory>
}
