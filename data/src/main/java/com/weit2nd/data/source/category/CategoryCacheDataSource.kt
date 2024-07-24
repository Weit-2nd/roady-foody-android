package com.weit2nd.data.source.category

import com.weit2nd.domain.model.spot.FoodSpotCategory

object CategoryCacheDataSource {
    private var categories: List<FoodSpotCategory> = emptyList()

    fun isCategoriesNotExist(): Boolean = categories.isEmpty()

    fun getCategories(): List<FoodSpotCategory> {
        return categories
    }

    fun setCategories(categories: List<FoodSpotCategory>) {
        this.categories = categories.toList()
    }
}
