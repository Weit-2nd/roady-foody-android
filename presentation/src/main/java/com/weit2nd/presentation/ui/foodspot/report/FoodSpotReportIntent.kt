package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.spot.OperationHour
import java.time.LocalTime

sealed class FoodSpotReportIntent {
    data object GetFoodSpotCategories : FoodSpotReportIntent()

    data class ChangeNameState(
        val name: String,
    ) : FoodSpotReportIntent()

    data object NavToSelectPlace : FoodSpotReportIntent()

    data class SetPlace(
        val place: Place,
    ) : FoodSpotReportIntent()

    data class ChangeFoodTruckState(
        val isFoodTruck: Boolean,
    ) : FoodSpotReportIntent()

    data class ChangeOpenState(
        val isOpen: Boolean,
    ) : FoodSpotReportIntent()

    data class ChangeOperationHourStatus(
        val operationHourStatus: OperationHourStatus,
        val isSelected: Boolean,
    ) : FoodSpotReportIntent()

    data class OpenTimePickerDialog(
        val operationHour: OperationHour,
        val isOpeningTime: Boolean,
    ) : FoodSpotReportIntent()

    data object CloseTimePickerDialog : FoodSpotReportIntent()

    data class ChangeOperationTime(
        val operationHour: OperationHour,
        val isOpeningTime: Boolean,
        val selectedTime: LocalTime,
    ) : FoodSpotReportIntent()

    data class ChangeCategoryStatus(
        val categoryStatus: CategoryStatus,
    ) : FoodSpotReportIntent()

    data object SelectImage : FoodSpotReportIntent()

    data class DeleteImage(
        val imgUri: String,
    ) : FoodSpotReportIntent()

    data object ReportFoodSpot : FoodSpotReportIntent()
}
