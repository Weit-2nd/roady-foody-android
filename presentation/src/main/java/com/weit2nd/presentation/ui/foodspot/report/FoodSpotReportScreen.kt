package com.weit2nd.presentation.ui.foodspot.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodSpotReportScreen(
    vm: FoodSpotReportViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state = vm.collectAsState()

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
                value = "음식점이름",
                onValueChange = {},
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
                        RadioButton(selected = true, onClick = { })
                        Text(text = "영업중")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = false, onClick = { })
                        Text(text = "폐업")
                    }
                }
            }

            Column {
                val categories = listOf("카테고리테스트입니다하나둘셋", "ㅁㅁㅁ", "ㅁㅁ", "ㅁ", "dfdfd")
                Text(text = "음식 카테고리")
                Text(text = "*최소 1개 이상 선택해야 합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    categories.forEach {
                        SuggestionChip(onClick = { }, label = { Text(it) })
                    }
                }
            }

            Column {
                Text(text = "음식점 사진")
                Text(text = "*최대 3개까지 등록 가능합니다.", fontSize = 12.sp, fontStyle = FontStyle.Italic)
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                )
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
