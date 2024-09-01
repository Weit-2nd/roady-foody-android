package com.weit2nd.presentation.ui.foodspot.report

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.SelectPlaceRoutes
import com.weit2nd.presentation.navigation.dto.PlaceDTO
import com.weit2nd.presentation.navigation.dto.toPlace
import com.weit2nd.presentation.ui.common.CancelableImage
import com.weit2nd.presentation.ui.foodspot.report.FoodSpotReportViewModel.Companion.IMAGE_MAX_SIZE
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Primary
import com.weit2nd.presentation.ui.theme.Typography
import com.weit2nd.presentation.ui.theme.White
import com.weit2nd.presentation.util.ObserveSavedState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.DayOfWeek
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

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier =
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1.0f).padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
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
                            Text(
                                text = "영업 시간 입력",
                                style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Black,
                            )
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
                        Text(
                            text = "음식 카테고리",
                            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Black,
                        )
                        Text(
                            text = "*최소 1개 이상 선택해주세요",
                            style = Typography.bodyMedium,
                            color = Gray2,
                        )
                        FoodCategory(
                            categories = state.value.categories,
                            onClickCategory = vm::onClickCategory,
                        )
                    }

                    Column {
                        Text(
                            text = "음식점 사진",
                            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Black,
                        )
                        Text(
                            text = "*최대 3개까지 등록 가능합니다",
                            style = Typography.bodyMedium,
                            color = Gray2,
                        )
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        FoodSpotImage(
                            reportImages = state.value.reportImages,
                            onDeleteImage = vm::onDeleteImage,
                            onClickSelectImagesBtn = vm::onClickSelectImagesBtn,
                        )
                    }
                }
                ReportButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { vm.onClickReportBtn() },
                    enabled = state.value.isLoading.not(),
                )
            }
        },
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    navController.ObserveSavedState<PlaceDTO>(
        lifecycleOwner = lifecycleOwner,
        key = SelectPlaceRoutes.SELECT_PLACE_KEY,
    ) {
        vm.onSelectPlace(it.toPlace())
    }
}

@Composable
private fun TopBar(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.padding(start = 4.dp),
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "",
            )
        }
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .weight(1f),
            text = "음식점 제보하기",
            style = Typography.headlineSmall,
        )
    }
}

@Composable
private fun NameTextField(
    name: String,
    onNameValueChange: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue(name)) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = userInput,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = { Text(text = "음식점 이름") },
        onValueChange = { newValue ->
            userInput = newValue
            onNameValueChange(newValue.text)
        },
        textStyle = Typography.bodyLarge,
        singleLine = true,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
        supportingText = {
            Text(
                style = Typography.bodyMedium,
                color = Gray1,
                text = "1자리~20자리 이내로 입력해주세요",
            )
        },
    )
}

@Composable
private fun PlacementBtn(
    onClickSetPlaceBtn: () -> Unit,
    longitude: Double?,
    latitude: Double?,
) {
    Column {
        Text(
            text = "위치 설정",
            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var address = "$longitude | $latitude"
            if (longitude == null || latitude == null) {
                address = "주소 입력"
            }
            Text(text = address, style = Typography.bodyLarge, color = Gray1)
            IconButton(onClick = { onClickSetPlaceBtn() }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "",
                    tint = Gray2,
                )
            }
        }
    }
}

@Composable
private fun FoodTruckSwitch(
    isFoodTruck: Boolean,
    onSwitchCheckedChange: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = "푸드트럭 여부 설정",
            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Text(
            text = "*푸드트럭이란 어쩌구저쩌구 입니다",
            style = Typography.bodyMedium,
            color = Gray2,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                colors =
                    androidx.compose.material3.CheckboxDefaults
                        .colors(checkedColor = Primary),
                checked = isFoodTruck,
                onCheckedChange = { onSwitchCheckedChange(it) },
            )
            Text(
                text = "푸드트럭 여부",
                style = Typography.bodyLarge,
                color = Black,
            )
        }
    }
}

@Composable
private fun OpenCloseSelector(
    isOpen: Boolean,
    onClickIsOpenBtn: (Boolean) -> Unit,
) {
    Column {
        Text(
            text = "현재 영업 여부 설정",
            style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Row(
            modifier = Modifier.selectableGroup(),
        ) {
            PrimaryRadioButton(
                selected = isOpen,
                onClick = { onClickIsOpenBtn(true) },
                text = "영업중",
            )
            Spacer(modifier = Modifier.padding(horizontal = 28.dp))
            PrimaryRadioButton(
                selected = isOpen.not(),
                onClick = { onClickIsOpenBtn(false) },
                text = "폐업",
            )
        }
    }
}

@Composable
private fun PrimaryRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            colors = RadioButtonDefaults.colors(selectedColor = Primary, unselectedColor = Primary),
            selected = selected,
            onClick = { onClick() },
        )
        Text(
            text = text,
            style = Typography.bodyLarge,
            color = Black,
        )
    }
}

@Composable
private fun OperationTimeSetting(
    operationHours: List<OperationHourStatus>,
    dialogStatus: TimePickerDialogStatus,
    onClickDayOfWeekBtn: (OperationHourStatus, isSelected: Boolean) -> Unit,
    onSelectTime: (operationHour: OperationHour, isOpeningTime: Boolean, selectedTime: LocalTime) -> Unit,
    onCloseDialog: () -> Unit,
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
) {
    val dayOfWeekTitle = listOf("월", "화", "수", "목", "금", "토", "일")
    dialogStatus.apply {
        if (isDialogOpen) {
            TimePickerDialog(
                selectedTime = if (isOpeningTime) operationHour.openingHours else operationHour.closingHours,
                onClickConfirm = { selectedTime ->
                    onSelectTime(operationHour, isOpeningTime, selectedTime)
                },
                onDismissRequest = onCloseDialog,
            )
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    DayOfWeekSelector(operationHours, onClickDayOfWeekBtn, dayOfWeekTitle)
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    operationHours.forEach { operationHourStatus ->
        if (operationHourStatus.isSelected) {
            TimeSelector(dayOfWeekTitle, operationHourStatus, onClickEditTimeBtn)
        }
    }
}

@Composable
private fun DayOfWeekSelector(
    operationHours: List<OperationHourStatus>,
    onClickDayOfWeekBtn: (OperationHourStatus, isSelected: Boolean) -> Unit,
    dayOfWeekTitle: List<String>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        operationHours.forEach { operationHourStatus ->
            FilterChip(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = White,
                    ),
                border = BorderStroke(1.dp, Primary),
                selected = operationHourStatus.isSelected,
                onClick = {
                    onClickDayOfWeekBtn(
                        operationHourStatus,
                        operationHourStatus.isSelected.not(),
                    )
                },
                label = {
                    Text(
                        textAlign = TextAlign.Center,
                        text = dayOfWeekTitle[operationHourStatus.operationHour.dayOfWeek.value - 1],
                        style = Typography.headlineSmall,
                    )
                },
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
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = dayOfWeekTitle[operationHourStatus.operationHour.dayOfWeek.value - 1],
            style = Typography.headlineSmall,
            color = Black,
        )
        Spacer(modifier = Modifier.padding(horizontal = 22.dp))
        OperationTime(onClickEditTimeBtn, operationHourStatus, true)
        Text(
            text = " ~ ",
            style = Typography.titleMedium,
            color = Black,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val hours =
            operationHourStatus.operationHour.run {
                if (isOpeningTime) {
                    openingHours
                } else {
                    closingHours
                }
            }

        Icon(
            painter = painterResource(id = R.drawable.ic_outline_clock),
            tint = Gray1,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = hours.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = Typography.titleMedium,
            color = Black,
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FoodCategory(
    categories: List<CategoryStatus>,
    onClickCategory: (CategoryStatus) -> Unit,
) {
    FlowRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach { categoryStatus ->
            FilterChip(
                onClick = { onClickCategory(categoryStatus) },
                selected = categoryStatus.isChecked,
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary,
                        selectedLabelColor = White,
                    ),
                border = BorderStroke(1.dp, Primary),
                label = {
                    Text(
                        categoryStatus.category.name,
                        style = Typography.titleSmall,
                    )
                },
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
            Image(
                modifier =
                    Modifier.clickable {
                        onClickSelectImagesBtn()
                    },
                painter = painterResource(id = R.drawable.ic_add_image),
                contentDescription = "",
            )
        }
        reportImages.forEach { imgUri ->
            CancelableImage(
                modifier = Modifier.size(80.dp),
                imgUri = imgUri,
                onDeleteImage = onDeleteImage,
            )
        }
    }
}

@Composable
private fun ReportButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        onClick = { onClick() },
        enabled = enabled,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = "음식점 등록하기",
            style = Typography.headlineSmall,
            color = White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldPreview() {
    TopBar(
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true)
@Composable
private fun PlacementPreview() {
    PlacementBtn(
        {},
        null,
        null,
    )
}

@Preview(showBackground = true)
@Composable
private fun FoodTruckPreview() {
    FoodTruckSwitch(isFoodTruck = false) {}
}

@Preview(showBackground = true)
@Composable
private fun OpenClosePreview() {
    OpenCloseSelector(true) {}
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPreview() {
    DayOfWeekSelector(
        operationHours =
            DayOfWeek.entries.map { dayOfWeek ->
                OperationHourStatus(
                    OperationHour(
                        dayOfWeek = dayOfWeek,
                        openingHours = LocalTime.of(9, 0),
                        closingHours = LocalTime.of(18, 0),
                    ),
                )
            },
        onClickDayOfWeekBtn = { _, _ -> },
        dayOfWeekTitle = listOf("월", "화", "수", "목", "금", "토", "일"),
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeSelectorPreview() {
    TimeSelector(
        dayOfWeekTitle = listOf("월", "화", "수", "목", "금", "토", "일"),
        operationHourStatus =
            OperationHourStatus(
                OperationHour(
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(12, 0),
                ),
                true,
            ),
        onClickEditTimeBtn = { _, _ -> },
    )
}

@Preview(showBackground = true)
@Composable
private fun FoodCategoryPreview() {
    FoodCategory(
        listOf(CategoryStatus(FoodCategory(0L, "포장마차"), true)),
        onClickCategory = { _ -> },
    )
}

@Preview(showBackground = true)
@Composable
private fun ReportButtonPreview() {
    ReportButton(modifier = Modifier.fillMaxWidth(), onClick = { }, enabled = true)
}
