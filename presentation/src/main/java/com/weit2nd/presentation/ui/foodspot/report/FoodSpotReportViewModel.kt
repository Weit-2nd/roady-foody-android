package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.usecase.pickimage.PickMultipleImagesUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FoodSpotReportViewModel @Inject constructor(
    private val pickMultipleImagesUseCase: PickMultipleImagesUseCase,
) : BaseViewModel<FoodSpotReportState, FoodSpotReportSideEffect>() {
    override val container =
        container<FoodSpotReportState, FoodSpotReportSideEffect>(FoodSpotReportState())

    fun onCreate() {
        FoodSpotReportIntent.GetFoodSpotCategories.post()
    }

    fun onNameValueChange(name: String) {
        FoodSpotReportIntent.ChangeNameState(name).post()
    }

    fun onClickSetPlaceBtn() {
        FoodSpotReportIntent.NavToSelectPlace.post()
    }

    fun onSelectPlace(place: Place) {
        FoodSpotReportIntent.SetPlace(place).post()
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        FoodSpotReportIntent.ChangeFoodTruckState(isChecked).post()
    }

    fun onClickIsOpenBtn(isOpen: Boolean) {
        FoodSpotReportIntent.ChangeOpenState(isOpen).post()
    }

    fun onClickDayOfWeekBtn(operationHourStatus: OperationHourStatus) {
        FoodSpotReportIntent.ChangeOperationHourStatus(operationHourStatus).post()
    }

    fun onClickEditTimeBtn(
        operationHour: OperationHour,
        isOpeningTime: Boolean,
    ) {
        FoodSpotReportIntent.OpenTimePickerDialog(operationHour, isOpeningTime).post()
    }

    fun onCloseDialog() {
        FoodSpotReportIntent.CloseTimePickerDialog.post()
    }

    fun onSelectTime(
        operationHour: OperationHour,
        isOpeningTime: Boolean,
        selectedTime: LocalTime,
    ) {
        FoodSpotReportIntent.ChangeOperationTime(operationHour, isOpeningTime, selectedTime).post()
    }

    fun onClickCategory(categoryStatus: CategoryStatus) {
        FoodSpotReportIntent.ChangeCategoryStatus(categoryStatus).post()
    }

    fun onClickSelectImagesBtn() {
        FoodSpotReportIntent.SelectImage.post()
    }

    fun onDeleteImage(imgUri: String) {
        FoodSpotReportIntent.DeleteImage(imgUri).post()
    }

    private fun FoodSpotReportIntent.post() =
        intent {
            when (this@post) {
                FoodSpotReportIntent.GetFoodSpotCategories -> {
                    // todo 카테고리 조회 api 연결
                    val categories =
                        listOf(
                            FoodSpotCategory(1, "붕어빵"),
                            FoodSpotCategory(1, "붕어빵1"),
                            FoodSpotCategory(1, "붕어빵22"),
                            FoodSpotCategory(1, "붕어빵333"),
                        )
                    reduce {
                        state.copy(
                            categories = categories.map { CategoryStatus(it) },
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeNameState -> {
                    reduce {
                        state.copy(
                            name = name,
                        )
                    }
                }

                FoodSpotReportIntent.NavToSelectPlace -> {
                    postSideEffect(FoodSpotReportSideEffect.NavToSelectPlace)
                }

                is FoodSpotReportIntent.SetPlace -> {
                    reduce {
                        state.copy(
                            place = place,
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeFoodTruckState -> {
                    reduce {
                        state.copy(
                            isFoodTruck = isFoodTruck,
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeOpenState -> {
                    reduce {
                        state.copy(
                            isOpen = isOpen,
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeOperationHourStatus -> {
                    reduce {
                        state.copy(
                            operationHours =
                                state.operationHours.map { status ->
                                    if (status == operationHourStatus) {
                                        status.copy(isSelected = status.isSelected.not())
                                    } else {
                                        status
                                    }
                                },
                        )
                    }
                }

                is FoodSpotReportIntent.OpenTimePickerDialog -> {
                    reduce {
                        state.copy(
                            dialogStatus =
                                state.dialogStatus.copy(
                                    isDialogOpen = true,
                                    operationHour = operationHour,
                                    isOpeningTime = isOpeningTime,
                                ),
                        )
                    }
                }

                FoodSpotReportIntent.CloseTimePickerDialog -> {
                    reduce { state.copy(dialogStatus = state.dialogStatus.copy(isDialogOpen = false)) }
                }

                is FoodSpotReportIntent.ChangeOperationTime -> {
                    reduce {
                        state.copy(
                            operationHours =
                                state.operationHours.map { status ->
                                    if (status.operationHour == operationHour) {
                                        if (isOpeningTime) {
                                            status.copy(operationHour = operationHour.copy(openingHours = selectedTime))
                                        } else {
                                            status.copy(operationHour = operationHour.copy(closingHours = selectedTime))
                                        }
                                    } else {
                                        status
                                    }
                                },
                            dialogStatus = state.dialogStatus.copy(isDialogOpen = false),
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeCategoryStatus -> {
                    reduce {
                        state.copy(
                            categories =
                                state.categories.map { category ->
                                    if (category == categoryStatus) {
                                        category.copy(isChecked = category.isChecked.not())
                                    } else {
                                        category
                                    }
                                },
                        )
                    }
                }

                FoodSpotReportIntent.SelectImage -> {
                    val selectedImages =
                        pickMultipleImagesUseCase.invoke(maximumSelect = IMAGE_MAX_SIZE - state.reportImages.size)
                    reduce {
                        state.copy(
                            reportImages = (state.reportImages + selectedImages).toSet().toList(),
                        )
                    }
                }

                is FoodSpotReportIntent.DeleteImage -> {
                    reduce {
                        state.copy(
                            reportImages = state.reportImages.filterNot { it == imgUri },
                        )
                    }
                }
            }
        }

    companion object {
        const val IMAGE_MAX_SIZE = 3
    }
}
