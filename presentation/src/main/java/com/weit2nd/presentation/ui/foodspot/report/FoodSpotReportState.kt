package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.model.spot.OperationHour
import java.time.DayOfWeek

data class FoodSpotReportState(
    val name: String = "",
    val place: Place? = null,
    val isFoodTruck: Boolean = false,
    val isOpen: Boolean = true,
    val operationHours: List<OperationHourStatus> =
        DayOfWeek.entries.map { dayOfWeek ->
            OperationHourStatus(
                OperationHour(dayOfWeek = dayOfWeek),
            )
        },
    val dialogStatus: TimePickerDialogStatus = TimePickerDialogStatus(),
    val categories: List<CategoryStatus> = emptyList(),
    val reportImages: List<String> = emptyList(),
)

data class CategoryStatus(
    val category: FoodSpotCategory,
    val isChecked: Boolean = false,
)

data class OperationHourStatus(
    val operationHour: OperationHour,
    val isSelected: Boolean = true,
)

data class TimePickerDialogStatus(
    val isDialogOpen: Boolean = false,
    val operationHour: OperationHour = OperationHour(DayOfWeek.MONDAY),
    val isOpeningTime: Boolean = true,
)
