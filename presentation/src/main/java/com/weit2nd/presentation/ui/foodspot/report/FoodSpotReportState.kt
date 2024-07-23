package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.model.spot.OperationHour
import java.time.DayOfWeek
import java.time.LocalTime

data class FoodSpotReportState(
    val name: String = "",
    val place: Place? = null,
    val isFoodTruck: Boolean = false,
    val isOpen: Boolean = true,
    val operationHours: List<OperationHourStatus> =
        DayOfWeek.entries.map { dayOfWeek ->
            OperationHourStatus(
                OperationHour(
                    dayOfWeek = dayOfWeek,
                    openingHours = LocalTime.of(9, 0),
                    closingHours = LocalTime.of(18, 0),
                ),
            )
        },
    val dialogStatus: TimePickerDialogStatus = TimePickerDialogStatus(),
    val categories: List<CategoryStatus> = emptyList(),
    val reportImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
)

data class CategoryStatus(
    val category: FoodSpotCategory,
    val isChecked: Boolean = false,
)

data class OperationHourStatus(
    val operationHour: OperationHour,
    val isSelected: Boolean = true,
)

private val operationHourDummy =
    OperationHour(
        dayOfWeek = DayOfWeek.MONDAY,
        openingHours = LocalTime.of(9, 0),
        closingHours = LocalTime.of(18, 0),
    )

data class TimePickerDialogStatus(
    val isDialogOpen: Boolean = false,
    val operationHour: OperationHour = operationHourDummy,
    val isOpeningTime: Boolean = true,
)
