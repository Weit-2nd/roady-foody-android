package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.model.spot.ReportFoodSpotState
import com.weit2nd.domain.usecase.pickimage.PickMultipleImagesUseCase
import com.weit2nd.domain.usecase.spot.ReportFoodSpotUseCase
import com.weit2nd.domain.usecase.spot.VerifyReportUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class FoodSpotReportViewModel @Inject constructor(
    private val pickMultipleImagesUseCase: PickMultipleImagesUseCase,
    private val verifyReportUseCase: VerifyReportUseCase,
    private val reportFoodSpotUseCase: ReportFoodSpotUseCase,
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

    fun onClickDayOfWeekBtn(
        operationHourStatus: OperationHourStatus,
        isSelected: Boolean,
    ) {
        FoodSpotReportIntent
            .ChangeOperationHourStatus(
                operationHourStatus,
                isSelected,
            ).post()
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

    fun onClickReportBtn() {
        FoodSpotReportIntent.ReportFoodSpot.post()
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
                                        status.copy(isSelected = isSelected)
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

                FoodSpotReportIntent.ReportFoodSpot -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }

                    val selectedFoodCategories =
                        state.categories
                            .filter { it.isChecked }
                            .map { it.category.id }

                    val reportFoodSpotState =
                        state.run {
                            verifyReportUseCase.invoke(
                                name = name,
                                longitude = place?.longitude,
                                latitude = place?.latitude,
                                foodCategories = selectedFoodCategories,
                                images = reportImages,
                            )
                        }
                    when (reportFoodSpotState) {
                        ReportFoodSpotState.BadCoordinate -> {
                            postSideEffect(FoodSpotReportSideEffect.ShowToast("올바른 위치를 설정해주세요."))
                        }

                        ReportFoodSpotState.BadFoodSpotName -> {
                            postSideEffect(FoodSpotReportSideEffect.ShowToast("상호명은 1자 이상 20자 이하로 입력해주세요."))
                        }

                        ReportFoodSpotState.NoFoodCategory -> {
                            postSideEffect(FoodSpotReportSideEffect.ShowToast("음식 카테고리는 최소 1개 이상 선택해야 합니다."))
                        }

                        ReportFoodSpotState.TooManyImages -> {
                            postSideEffect(FoodSpotReportSideEffect.ShowToast("이미지는 최대 3개까지 업로드할 수 있습니다."))
                        }

                        is ReportFoodSpotState.InvalidImage -> {
                            postSideEffect(FoodSpotReportSideEffect.ShowToast("잘못된 이미지 파일입니다."))
                        }

                        ReportFoodSpotState.Valid -> {
                            state
                                .run {
                                    runCatching {
                                        reportFoodSpotUseCase
                                            .invoke(
                                                name = name,
                                                longitude = place?.longitude ?: 0.0,
                                                latitude = place?.latitude ?: 0.0,
                                                isFoodTruck = isFoodTruck,
                                                open = isOpen,
                                                closed = isOpen.not(),
                                                foodCategories = selectedFoodCategories,
                                                operationHours =
                                                    operationHours
                                                        .filter { it.isSelected }
                                                        .map { it.operationHour },
                                                images = reportImages,
                                            )
                                    }
                                }.onSuccess {
                                    postSideEffect(FoodSpotReportSideEffect.ShowToast("등록되었습니다."))
                                    postSideEffect(FoodSpotReportSideEffect.ReportSuccess)
                                }.onFailure { throwable ->
                                    throwable.message?.let {
                                        postSideEffect(
                                            FoodSpotReportSideEffect.ShowToast(
                                                it,
                                            ),
                                        )
                                    }
                                }
                        }
                    }
                    reduce {
                        state.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }

    companion object {
        const val IMAGE_MAX_SIZE = 3
    }
}
