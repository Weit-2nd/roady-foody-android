package com.weit2nd.data.source.category

import com.weit2nd.domain.model.spot.FoodCategory

object CategoryCacheDataSource {
    private var categories: List<FoodCategory> = emptyList()

    fun isCategoriesNotExist(): Boolean = categories.isEmpty()

    fun getCategories(): List<FoodCategory> {
        return categories
    }

    fun setCategories(categories: List<FoodCategory>) {
        this.categories = categories.toList()
    }
}
