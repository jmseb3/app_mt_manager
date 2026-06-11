package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private enum class DateStep(
    val label: String,
    val period: DatePeriod
) {
    Year("년", DatePeriod(years = 1)),
    Month("월", DatePeriod(months = 1)),
    Day("일", DatePeriod(days = 1))
}

@OptIn(ExperimentalTime::class)
private fun today(): LocalDate {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}

@OptIn(ExperimentalTime::class)
private fun Long.toLocalDate(): LocalDate {
    return kotlin.time.Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date
}

@OptIn(ExperimentalTime::class)
private fun LocalDate.toUtcMillis(): Long {
    return kotlin.time.Instant.parse("${this}T00:00:00Z").toEpochMilliseconds()
}

private fun LocalDate.toDateString(): String {
    return listOf(
        year.toString().padStart(4, '0'),
        month.number.toString().padStart(2, '0'),
        day.toString().padStart(2, '0')
    ).joinToString(".")
}

private fun String.toLocalDateOrNull(): LocalDate? {
    val values = split(".").mapNotNull { it.toIntOrNull() }
    if (values.size != 3) return null
    return runCatching {
        LocalDate(values[0], values[1], values[2])
    }.getOrNull()
}

@Composable
fun DateRangePickerDialog(
    initialStartDate: String = "",
    initialEndDate: String = "",
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit
) {
    val defaultStartDate = initialStartDate.toLocalDateOrNull() ?: today()
    var startDate by remember { mutableStateOf(defaultStartDate) }
    var endDate by remember {
        mutableStateOf(initialEndDate.toLocalDateOrNull() ?: defaultStartDate)
    }
    val rangeValid = startDate <= endDate

    SimpleDateDialog(
        title = "MT 일정을 선택하세요",
        confirmEnabled = rangeValid,
        onDismiss = onDismiss,
        onConfirm = {
            onDateSelected(startDate.toDateString(), endDate.toDateString())
            onDismiss()
        }
    ) {
        DateControl(
            title = "시작일",
            date = startDate,
            onDateChange = { startDate = it }
        )
        DateControl(
            title = "종료일",
            date = endDate,
            onDateChange = { endDate = it }
        )
        if (!rangeValid) {
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = "종료일은 시작일 이후로 선택해 주세요.",
                color = match1.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun OneDatePickerDialog(
    initialSelectedDateMillis: Long? = null,
    selectableDates: (Long) -> Boolean = { true },
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    var selectedDate by remember {
        mutableStateOf(initialSelectedDateMillis?.toLocalDate() ?: today())
    }

    SimpleDateDialog(
        title = "계획 일을 선택하세요",
        confirmEnabled = selectableDates(selectedDate.toUtcMillis()),
        onDismiss = onDismiss,
        onConfirm = {
            onDateSelected(selectedDate.toDateString())
            onDismiss()
        }
    ) {
        DateControl(
            title = "일자",
            date = selectedDate,
            selectableDates = selectableDates,
            onDateChange = { selectedDate = it }
        )
    }
}

@Composable
private fun SimpleDateDialog(
    title: String,
    confirmEnabled: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            DefaultText(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                color = match1,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                content()
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = confirmEnabled
            ) {
                DefaultText(
                    text = "확인",
                    color = if (confirmEnabled) match1 else match1.copy(alpha = 0.5f)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                DefaultText(
                    text = "닫기",
                    color = match1
                )
            }
        },
        containerColor = match2
    )
}

@Composable
private fun DateControl(
    title: String,
    date: LocalDate,
    selectableDates: (Long) -> Boolean = { true },
    onDateChange: (LocalDate) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultText(
            modifier = Modifier.fillMaxWidth(),
            text = "$title  ${date.toDateString()}",
            color = match1,
            fontSize = 16.sp
        )
        DateStep.entries.forEach { step ->
            DateStepRow(
                step = step,
                date = date,
                selectableDates = selectableDates,
                onDateChange = onDateChange
            )
        }
    }
}

@Composable
private fun DateStepRow(
    step: DateStep,
    date: LocalDate,
    selectableDates: (Long) -> Boolean,
    onDateChange: (LocalDate) -> Unit
) {
    val previous = date.minus(step.period)
    val next = date.plus(step.period)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateStepButton(
            modifier = Modifier.weight(1f),
            text = "-${step.label}",
            enabled = selectableDates(previous.toUtcMillis()),
            onClick = { onDateChange(previous) }
        )
        DefaultText(
            modifier = Modifier.weight(1f),
            text = step.label,
            color = match1
        )
        DateStepButton(
            modifier = Modifier.weight(1f),
            text = "+${step.label}",
            enabled = selectableDates(next.toUtcMillis()),
            onClick = { onDateChange(next) }
        )
    }
}

@Composable
private fun DateStepButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val color = if (enabled) match1 else match1.copy(alpha = 0.4f)
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(1.dp, color),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 6.dp)
    ) {
        DefaultText(
            text = text,
            color = color
        )
    }
}
