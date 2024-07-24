package com.weit2nd.data.source.category

import com.weit2nd.data.model.category.CategoryDTO
import com.weit2nd.data.service.CategoryService
import javax.inject.Inject

class CategoryDataSource @Inject constructor(
    private val service: CategoryService,
) {
    suspend fun getCategories(): List<CategoryDTO> {
        return service.getCategories()
    }
}
