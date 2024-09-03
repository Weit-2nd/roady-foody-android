package com.weit2nd.presentation.ui.foodspot.report

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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
import com.weit2nd.presentation.ui.common.AddImageButton
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.common.CancelableImage
import com.weit2nd.presentation.ui.foodspot.report.FoodSpotReportViewModel.Companion.IMAGE_MAX_SIZE
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
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

            FoodSpotReportSideEffect.NavToBack -> {
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
                onClickBackBtn = vm::onClickBackBtn,
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
                            .weight(1.0f)
                            .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    NameTextField(
                        name = state.value.name,
                        onNameValueChange = vm::onNameValueChange,
                    )

                    PlacementBtn(
                        onClickSetPlaceBtn = vm::onClickSetPlaceBtn,
                        addressName = state.value.place?.addressName,
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
                                text = stringResource(R.string.food_spot_open_title),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
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

                    FoodCategoryContainer(
                        categories = state.value.categories,
                        onClickCategory = vm::onClickCategory,
                    )

                    FoodSpotImageContainer(
                        reportImages = state.value.reportImages,
                        onDeleteImage = vm::onDeleteImage,
                        onClickSelectImagesBtn = vm::onClickSelectImagesBtn,
                    )
                }
                BottomButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { vm.onClickReportBtn() },
                    enabled = state.value.isLoading.not(),
                    text = stringResource(R.string.food_spot_report_btn),
                    textStyle = MaterialTheme.typography.headlineSmall,
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
private fun TopBar(
    modifier: Modifier,
    onClickBackBtn: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = modifier,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.tood_spot_report_toolbar_title),
            style = MaterialTheme.typography.headlineSmall,
            color = Black,
        )
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier.padding(start = 4.dp),
                onClick = { onClickBackBtn() },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                    tint = Black,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameTextField(
    name: String,
    onNameValueChange: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue(name)) }

    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = userInput,
            onValueChange = { newValue ->
                userInput = newValue
                onNameValueChange(newValue.text)
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = userInput.text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Text(
                        text = stringResource(R.string.food_spot_name_placeholder),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Gray2),
                    )
                },
                container = { Color.Transparent },
                contentPadding = PaddingValues(0.dp),
            )
        }

        // Line below the BasicTextField
        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp),
            thickness = 1.dp,
            color = Gray2,
        )

        // Supporting text
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = Gray2,
            text = stringResource(R.string.food_spot_name_label),
        )
    }
}

@Composable
private fun PlacementBtn(
    onClickSetPlaceBtn: () -> Unit,
    addressName: String?,
) {
    Column {
        Text(
            text = stringResource(R.string.food_spot_place_title),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val address = addressName ?: stringResource(R.string.food_spot_place)
            Text(text = address, style = MaterialTheme.typography.bodyLarge, color = Gray1)
            IconButton(onClick = { onClickSetPlaceBtn() }) {
                Icon(
                    modifier = Modifier.size(24.dp),
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
            text = stringResource(R.string.food_spot_food_truck_title),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Text(
            text = stringResource(R.string.food_spot_food_truck_label),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray2,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                colors =
                    androidx.compose.material3.CheckboxDefaults
                        .colors(checkedColor = MaterialTheme.colorScheme.primary),
                checked = isFoodTruck,
                onCheckedChange = { onSwitchCheckedChange(it) },
            )
            Text(
                text = stringResource(R.string.food_spot_food_truck),
                style = MaterialTheme.typography.bodyLarge,
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
            text = stringResource(R.string.food_spot_open_setting_title),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Row(
            modifier = Modifier.selectableGroup(),
        ) {
            PrimaryRadioButton(
                selected = isOpen,
                onClick = { onClickIsOpenBtn(true) },
                text = stringResource(R.string.food_spot_isopen),
            )
            Spacer(modifier = Modifier.padding(horizontal = 28.dp))
            PrimaryRadioButton(
                selected = isOpen.not(),
                onClick = { onClickIsOpenBtn(false) },
                text = stringResource(R.string.food_spot_isclose),
            )
        }
    }
}

@Composable
private fun PrimaryRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.primary,
                ),
            selected = selected,
            onClick = { onClick() },
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
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
    DayOfWeekSelector(
        modifier = Modifier.fillMaxWidth(),
        operationHours,
        onClickDayOfWeekBtn,
        dayOfWeekTitle,
    )
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    operationHours.forEach { operationHourStatus ->
        if (operationHourStatus.isSelected) {
            TimeSelector(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                dayOfWeekTitle,
                operationHourStatus,
                onClickEditTimeBtn,
            )
        }
    }
}

@Composable
private fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    operationHours: List<OperationHourStatus>,
    onClickDayOfWeekBtn: (OperationHourStatus, isSelected: Boolean) -> Unit,
    dayOfWeekTitle: List<String>,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        operationHours.forEach { operationHourStatus ->
            FilterChip(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
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
                        style = MaterialTheme.typography.headlineSmall,
                    )
                },
            )
        }
    }
}

@Composable
private fun TimeSelector(
    modifier: Modifier = Modifier,
    dayOfWeekTitle: List<String>,
    operationHourStatus: OperationHourStatus,
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = dayOfWeekTitle[operationHourStatus.operationHour.dayOfWeek.value - 1],
            style = MaterialTheme.typography.headlineSmall,
            color = Black,
        )
        Spacer(modifier = Modifier.padding(horizontal = 22.dp))
        OperationTime(onClickEditTimeBtn, operationHourStatus, true)
        Text(
            text = "~",
            style = MaterialTheme.typography.titleMedium,
            color = Black,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        OperationTime(onClickEditTimeBtn, operationHourStatus, false)
    }
}

@Composable
private fun OperationTime(
    onClickEditTimeBtn: (operationHour: OperationHour, isOpeningTime: Boolean) -> Unit,
    operationHourStatus: OperationHourStatus,
    isOpeningTime: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.clickable {
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
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_outline_clock),
            tint = Gray1,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = hours.format(DateTimeFormatter.ofPattern(stringResource(R.string.food_spot_report_time_pattern))),
            style = MaterialTheme.typography.titleMedium,
            color = Black,
        )
    }
}

@Composable
private fun FoodCategoryContainer(
    modifier: Modifier = Modifier,
    categories: List<CategoryStatus>,
    onClickCategory: (CategoryStatus) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.food_category),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Text(
            text = stringResource(R.string.food_category_label),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray2,
        )
        FoodCategory(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
            categories = categories,
            onClickCategory = onClickCategory,
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FoodCategory(
    modifier: Modifier = Modifier,
    categories: List<CategoryStatus>,
    onClickCategory: (CategoryStatus) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        categories.forEach { categoryStatus ->
            FilterChip(
                onClick = { onClickCategory(categoryStatus) },
                selected = categoryStatus.isChecked,
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                label = {
                    Text(
                        categoryStatus.category.name,
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
            )
        }
    }
}

@Composable
private fun FoodSpotImageContainer(
    modifier: Modifier = Modifier,
    reportImages: List<String>,
    onDeleteImage: (String) -> Unit,
    onClickSelectImagesBtn: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.food_spot_image_title),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black,
        )
        Text(
            text = stringResource(R.string.food_spot_image_label),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray2,
        )
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        FoodSpotImage(
            reportImages = reportImages,
            onDeleteImage = onDeleteImage,
            onClickSelectImagesBtn = onClickSelectImagesBtn,
        )
    }
}

@Composable
private fun FoodSpotImage(
    modifier: Modifier = Modifier,
    reportImages: List<String>,
    onDeleteImage: (String) -> Unit,
    onClickSelectImagesBtn: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (reportImages.size < IMAGE_MAX_SIZE) {
            AddImageButton(
                modifier =
                    Modifier.size(80.dp).clickable {
                        onClickSelectImagesBtn()
                    },
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

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldPreview() {
    RoadyFoodyTheme {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun PlacementPreview() {
    RoadyFoodyTheme {
        PlacementBtn(
            {},
            "addressName",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodTruckPreview() {
    RoadyFoodyTheme {
        FoodTruckSwitch(isFoodTruck = false) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun OpenClosePreview() {
    RoadyFoodyTheme {
        OpenCloseSelector(true) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPreview() {
    RoadyFoodyTheme {
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
}

@Preview(showBackground = true)
@Composable
private fun TimeSelectorPreview() {
    RoadyFoodyTheme {
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
}

@Preview(showBackground = true)
@Composable
private fun FoodCategoryPreview() {
    RoadyFoodyTheme {
        FoodCategory(
            modifier = Modifier,
            listOf(CategoryStatus(FoodCategory(0L, "포장마차"), true)),
            onClickCategory = { _ -> },
        )
    }
}
