package com.weit2nd.presentation.ui.foodspot.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.ui.common.CancelableImage
import com.weit2nd.presentation.ui.foodspot.report.FoodSpotReportViewModel.Companion.IMAGE_MAX_SIZE
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodSpotReportScreen(
    vm: FoodSpotReportViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state = vm.collectAsState()

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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.value.name,
                onValueChange = { vm.onNameValueChange(it) },
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(onClick = { }) {
                    Text(text = "위치설정")
                }
                Text(text = "longitude\nlatitude")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "푸드트럭 여부")
                Switch(
                    checked = state.value.isFoodTruck,
                    onCheckedChange = { vm.onSwitchCheckedChange(it) },
                )
            }

            Column {
                Text(text = "현재 영업여부")
                Row(
                    modifier = Modifier.selectableGroup(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = state.value.isOpen,
                            onClick = { vm.onClickIsOpenBtn(true) },
                        )
                        Text(text = "영업중")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = state.value.isOpen.not(),
                            onClick = { vm.onClickIsOpenBtn(false) },
                        )
                        Text(text = "폐업")
                    }
                }
            }

            Column {
                Text(text = "음식 카테고리")
                Text(text = "*최소 1개 이상 선택해야 합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.value.categories.forEach { categoryStatus ->
                        FilterChip(
                            onClick = { vm.onClickCategory(categoryStatus) },
                            selected = categoryStatus.isChecked,
                            label = { Text(categoryStatus.category.name) },
                        )
                    }
                }
            }

            Column {
                Text(text = "음식점 사진")
                Text(text = "*최대 3개까지 등록 가능합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.value.reportImages.forEach { imgUri ->
                        CancelableImage(
                            modifier = Modifier.size(100.dp),
                            imgUri = imgUri,
                            onDeleteImage = vm::onDeleteImage,
                        )
                    }
                    if (state.value.reportImages.size < IMAGE_MAX_SIZE) {
                        IconButton(
                            modifier = Modifier.size(100.dp).background(Color.LightGray),
                            onClick = vm::onClickSelectImagesBtn,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "select_image",
                            )
                        }
                    }
                }
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = { }) {
            Text(text = "음식점 등록하기")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodSpotReportScreenPreview() {
    FoodSpotReportScreen {}
}
