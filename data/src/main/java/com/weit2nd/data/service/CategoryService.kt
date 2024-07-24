package com.weit2nd.data.service

import com.weit2nd.data.model.category.CategoryDTO
import retrofit2.http.GET

interface CategoryService {
    @GET("/api/v1/food-categories")
    suspend fun getCategories(): List<CategoryDTO>
}
