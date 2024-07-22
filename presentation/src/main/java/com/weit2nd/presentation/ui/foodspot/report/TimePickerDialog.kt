package com.weit2nd.presentation.ui.foodspot.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.weit2nd.domain.model.spot.OperationHour
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    selectedTime: LocalTime,
    operationHour: OperationHour,
    isOpeningTime: Boolean,
    onDismissRequest: () -> Unit,
    onClickConfirm: (
        operationHour: OperationHour,
        isOpeningTime: Boolean,
        selectedTime: LocalTime,
    ) -> Unit,
) {
    val timePickerState =
        rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
            is24Hour = true,
        )

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TimePicker(
                modifier = Modifier.fillMaxWidth(),
                state = timePickerState,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { onDismissRequest() }) {
                    Text(text = "취소")
                }

                Button(
                    onClick = {
                        onClickConfirm(
                            operationHour,
                            isOpeningTime,
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute,
                            ),
                        )
                    },
                ) {
                    Text(text = "확인")
                }
            }
        }
    }
}
