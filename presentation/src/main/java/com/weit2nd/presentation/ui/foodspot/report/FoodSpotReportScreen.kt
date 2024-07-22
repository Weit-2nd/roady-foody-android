package com.weit2nd.presentation.ui.foodspot.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.presentation.navigation.SelectPlaceRoutes
import com.weit2nd.presentation.navigation.dto.PlaceDTO
import com.weit2nd.presentation.navigation.dto.toPlace
import com.weit2nd.presentation.ui.common.CancelableImage
import com.weit2nd.presentation.ui.foodspot.report.FoodSpotReportViewModel.Companion.IMAGE_MAX_SIZE
import com.weit2nd.presentation.util.ObserveSavedState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FoodSpotReportScreen(
    vm: FoodSpotReportViewModel = hiltViewModel(),
    navToSelectPlace: () -> Unit,
    navToBack: () -> Unit,
    navController: NavController,
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            FoodSpotReportSideEffect.NavToSelectPlace -> {
                navToSelectPlace()
            }

            is FoodSpotReportSideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            FoodSpotReportSideEffect.ReportSuccess -> {
                navToBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            NameTextField(
                name = state.value.name,
                onNameValueChange = vm::onNameValueChange,
            )

            PlacementBtn(
                onClickSetPlaceBtn = vm::onClickSetPlaceBtn,
                longitude = state.value.place?.longitude,
                latitude = state.value.place?.latitude,
            )

            FoodTruckSwitch(
                isFoodTruck = state.value.isFoodTruck,
                onSwitchCheckedChange = vm::onSwitchCheckedChange,
            )

            OpenCloseSelector(
                isOpen = state.value.isOpen,
                onClickIsOpenBtn = vm::onClickIsOpenBtn,
            )

            if (state.value.isOpen) {
                Column {
                    Text(text = "영업 시간 입력")
                    OperationTimeSetting(
                        operationHours = state.value.operationHours,
                        dialogStatus = state.value.dialogStatus,
                        onClickDayOfWeekBtn = vm::onClickDayOfWeekBtn,
                        onSelectTime = vm::onSelectTime,
                        onCloseDialog = vm::onCloseDialog,
                        onClickEditTimeBtn = vm::onClickEditTimeBtn,
                    )
                }
            }

            Column {
                Text(text = "음식 카테고리")
                Text(text = "*최소 1개 이상 선택해야 합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                FoodCategory(
                    categories = state.value.categories,
                    onClickCategory = vm::onClickCategory,
                )
            }

            Column {
                Text(text = "음식점 사진")
                Text(text = "*최대 3개까지 등록 가능합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                FoodSpotImage(
                    reportImages = state.value.reportImages,
                    onDeleteImage = vm::onDeleteImage,
                    onClickSelectImagesBtn = vm::onClickSelectImagesBtn,
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { vm.onClickReportBtn() },
            enabled = state.value.isLoading.not(),
        ) {
            Text(text = "음식점 등록하기")
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    navController.ObserveSavedState<PlaceDTO>(
        lifecycleOwner = lifecycleOwner,
        key = SelectPlaceRoutes.SELECT_PLACE_KEY,
    ) {
        vm.onSelectPlace(it.toPlace())
    }
}

@Composable
private fun NameTextField(
    name: String,
    onNameValueChange: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = { onNameValueChange(it) },
    )
}

@Composable
private fun PlacementBtn(
    onClickSetPlaceBtn: () -> Unit,
    longitude: Double?,
    latitude: Double?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(onClick = { onClickSetPlaceBtn() }) {
            Text(text = "위치설정")
        }
        Text(text = "$longitude\n$latitude")
    }
}

@Composable
private fun FoodTruckSwitch(
    isFoodTruck: Boolean,
    onSwitchCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "푸드트럭 여부")
        Switch(
            checked = isFoodTruck,
            onCheckedChange = { onSwitchCheckedChange(it) },
        )
    }
}

@Composable
private fun OpenCloseSelector(
    isOpen: Boolean,
    onClickIsOpenBtn: (Boolean) -> Unit,
) {
    Column {
        Text(text = "현재 영업여부")
        Row(
            modifier = Modifier.selectableGroup(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isOpen,
                    onClick = { onClickIsOpenBtn(true) },
                )
                Text(text = "영업중")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = isOpen.not(),
                    onClick = { onClickIsOpenBtn(false) },
                )
                Text(text = "폐업")
            }
        }
    }
}

@Composable
private fun OperationTimeSetting(
    operationHours: List<OperationHourStatus>,
    dialogStatus: TimePickerDialogStatus,
    onClickDayOfWeekBtn: (OperationHourStatus) -> Unit,
    onSelectTime: (operationHour: OperationHour, isOpeningTime: Boolean, selectedTime: LocalTime) -> Unit,
    onCloseDialog: () -> Unit,
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
) {
    val dayOfWeekTitle = listOf("월", "화", "수", "목", "금", "토", "일")
    dialogStatus.apply {
        if (isDialogOpen) {
            TimePickerDialog(
                selectedTime = if (isOpeningTime) operationHour.openingHours else operationHour.closingHours,
                operationHour = operationHour,
                isOpeningTime = isOpeningTime,
                onClickConfirm = onSelectTime,
                onDismissRequest = onCloseDialog,
            )
        }
    }
    DayOfWeekSelector(operationHours, onClickDayOfWeekBtn, dayOfWeekTitle)
    operationHours.forEach { operationHourStatus ->
        if (operationHourStatus.isSelected) {
            TimeSelector(dayOfWeekTitle, operationHourStatus, onClickEditTimeBtn)
        }
    }
}

@Composable
private fun DayOfWeekSelector(
    operationHours: List<OperationHourStatus>,
    onClickDayOfWeekBtn: (OperationHourStatus) -> Unit,
    dayOfWeekTitle: List<String>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        operationHours.forEach { operationHourStatus ->
            FilterChip(
                selected = operationHourStatus.isSelected,
                onClick = { onClickDayOfWeekBtn(operationHourStatus) },
                label = { Text(text = dayOfWeekTitle[operationHourStatus.operationHour.dayOfWeek.value - 1]) },
            )
        }
    }
}

@Composable
private fun TimeSelector(
    dayOfWeekTitle: List<String>,
    operationHourStatus: OperationHourStatus,
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
) {
    Row {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = dayOfWeekTitle[operationHourStatus.operationHour.dayOfWeek.value - 1],
        )
        OperationTime(onClickEditTimeBtn, operationHourStatus, true)
        Text(text = " ~ ", modifier = Modifier.padding(horizontal = 4.dp))
        OperationTime(onClickEditTimeBtn, operationHourStatus, false)
    }
}

@Composable
private fun OperationTime(
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
    operationHourStatus: OperationHourStatus,
    isOpeningTime: Boolean,
) {
    Row(
        modifier =
            Modifier.clickable {
                onClickEditTimeBtn(operationHourStatus.operationHour, isOpeningTime)
            },
    ) {
        val hours =
            operationHourStatus.operationHour.run {
                if (isOpeningTime) {
                    openingHours
                } else {
                    closingHours
                }
            }

        Text(
            text = hours.format(DateTimeFormatter.ofPattern("HH:mm")),
            textDecoration = TextDecoration.Underline,
        )
        Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FoodCategory(
    categories: List<CategoryStatus>,
    onClickCategory: (CategoryStatus) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach { categoryStatus ->
            FilterChip(
                onClick = { onClickCategory(categoryStatus) },
                selected = categoryStatus.isChecked,
                label = { Text(categoryStatus.category.name) },
            )
        }
    }
}

@Composable
private fun FoodSpotImage(
    reportImages: List<String>,
    onDeleteImage: (String) -> Unit,
    onClickSelectImagesBtn: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (reportImages.size < IMAGE_MAX_SIZE) {
            IconButton(
                modifier =
                    Modifier
                        .size(100.dp)
                        .background(Color.LightGray),
                onClick = onClickSelectImagesBtn,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "select_image",
                )
            }
        }
        reportImages.forEach { imgUri ->
            CancelableImage(
                modifier = Modifier.size(100.dp),
                imgUri = imgUri,
                onDeleteImage = onDeleteImage,
            )
        }
    }
}
