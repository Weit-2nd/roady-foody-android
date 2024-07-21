package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.spot.FoodSpotCategory

data class FoodSpotReportState(
    val name: String = "",
    val isFoodTruck: Boolean = false,
    val isOpen: Boolean = true,
    val categories: List<CategoryStatus> = emptyList(),
)

data class CategoryStatus(
    val category: FoodSpotCategory,
    val isChecked: Boolean = false,
)
