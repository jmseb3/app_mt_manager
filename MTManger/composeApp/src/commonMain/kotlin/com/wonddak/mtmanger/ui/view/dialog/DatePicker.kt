package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
private fun Long?.toDateString(): String {
    this ?: return ""
    val date =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.of("Asia/Seoul")).date
    val formatPattern = "yyyy.MM.dd"
    val dateTimeFormat = LocalDate.Format {
        byUnicodePattern(formatPattern)
    }
    return date.format(dateTimeFormat)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit
) {
    val datePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(
                        datePickerState.selectedStartDateMillis.toDateString(),
                        datePickerState.selectedEndDateMillis.toDateString()
                    )
                    onDismiss()
                },
                enabled = datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis != null
            ) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "닫기")
            }
        }
    ) {
        DateRangePicker(
            state = datePickerState,
            dateFormatter = remember {
                DatePickerDefaults.dateFormatter(
                    "yy MM dd",
                    "yy MM dd",
                    "yy MM dd"
                )
            },
            title = {
                Text(
                    text = "MT 일정을 선택하세요",
                    modifier = Modifier
                        .padding(16.dp)
                )
            },
            headline = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        Text(datePickerState.selectedStartDateMillis?.let {
                            it.toDateString()
                        } ?: "Start Date")
                    }
                    Box(Modifier.weight(1f)) {
                        Text(datePickerState.selectedEndDateMillis?.let {
                            it.toDateString()
                        } ?: "End Date")
                    }
                    Box(Modifier.weight(0.2f)) {
                        IconButton(
                            onClick = {
                                onDateSelected(
                                    datePickerState.selectedStartDateMillis.toDateString(),
                                    datePickerState.selectedEndDateMillis.toDateString()
                                )
                                onDismiss()
                            },
                            enabled = datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis != null
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "Ok")
                        }
                    }
                }
            },
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                containerColor = match2,
                dayInSelectionRangeContainerColor = match1.copy(alpha = 0.5f),
                dayInSelectionRangeContentColor = match2,
                selectedDayContainerColor = match1
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneDatePickerDialog(
    initialSelectedDateMillis : Long? = null,
    selectableDates: SelectableDates,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(
                        datePickerState.selectedDateMillis.toDateString(),
                    )
                    onDismiss()
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "닫기")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateFormatter = remember {
                DatePickerDefaults.dateFormatter(
                    "yy MM dd",
                    "yy MM dd",
                    "yy MM dd"
                )
            },
            title = {
                Text(
                    text = "계획 일을 선택하세요",
                    modifier = Modifier
                        .padding(16.dp)
                )
            },
            headline = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.weight(1f)) {
                        Text(datePickerState.selectedDateMillis?.let {
                            it.toDateString()
                        } ?: "Start Date")
                    }
                    Box(Modifier.weight(0.2f)) {
                        IconButton(
                            onClick = {
                                onDateSelected(
                                    datePickerState.selectedDateMillis.toDateString(),
                                )
                                onDismiss()
                            },
                            enabled = datePickerState.selectedDateMillis != null
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "Ok")
                        }
                    }
                }
            },
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                containerColor = match2,
                dayInSelectionRangeContainerColor = match1.copy(alpha = 0.5f),
                dayInSelectionRangeContentColor = match2,
                selectedDayContainerColor = match1
            )
        )
    }
}
