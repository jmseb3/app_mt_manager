package com.wonddak.mtmanger.ui.view.dialog

import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.toDateString
import com.wonddak.mtmanger.toDateStringInfos
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDialog(
    startDate: String,
    endDate: String,
    plan: Plan?,
    onAdd: (
        title: String,
        day: String,
        text: String
    ) -> Unit = { _, _, _ -> },
    onDismiss: () -> Unit = {}
) {

    var title by remember {
        mutableStateOf(plan?.nowplantitle ?: "")
    }
    var date by remember {
        mutableStateOf(plan?.nowday ?: "")
    }
    val context = LocalContext.current

    var planText by remember {
        mutableStateOf(plan?.simpletext ?: "")
    }
    val focusManager = LocalFocusManager.current
    var showPlanDatePicker by remember {
        mutableStateOf(false)
    }
    val startTime = Calendar.getInstance()
    startTime.set(
        startDate.split(".")[0].toInt(),
        startDate.split(".")[1].toInt() - 1,
        startDate.split(".")[2].toInt(),
        9,
        0,
        0,
    )
    val planeDatePicker = rememberDatePickerState(
        initialSelectedDateMillis = startTime.timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val start = Calendar.getInstance()
                start.set(
                    startDate.split(".")[0].toInt(),
                    startDate.split(".")[1].toInt() - 1,
                    startDate.split(".")[2].toInt(),
                    8,
                    0,
                    0,
                )
                val end = Calendar.getInstance()
                end.set(
                    endDate.split(".")[0].toInt(),
                    endDate.split(".")[1].toInt() - 1,
                    endDate.split(".")[2].toInt(),
                    9,
                    0,
                    0
                )
                Log.i("JWH", start.timeInMillis.toDateStringInfos())
                Log.i("JWH", utcTimeMillis.toDateStringInfos())
                Log.i("JWH", end.timeInMillis.toDateStringInfos())
                return (start.timeInMillis <= (utcTimeMillis) && (utcTimeMillis) <= end.timeInMillis)
            }

            override fun isSelectableYear(year: Int): Boolean {
                val start = startDate.split(".")[0].toInt()
                val end = endDate.split(".")[0].toInt()
                return (year == start || year == end)
            }
        }
    )
    DialogBase(
        titleText = if (plan != null) "계획 수정" else "계획 작성",
        confirmText = if (plan != null) "수정" else "추가",
        onConfirm = {
            if (title.isEmpty() || date.isEmpty() || planText.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.dialog_error_field),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onAdd(title, date, planText)
            }
        },
        onDismiss = onDismiss
    ) {
        Column {
            DialogTextField(
                value = date,
                placeHolder = "일자를 선택해 주세요",
                label = "일자",
                change = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showPlanDatePicker = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = null,
                            tint = match1,
                        )
                    }
                }
            )
            DialogTextField(
                value = title,
                placeHolder = "제목을 입력해 주세요",
                label = "제목",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            ) {
                title = it
            }
            DialogTextField(
                value = planText,
                placeHolder = "계획을 입력해 주세요",
                label = "계획",
                modifier = Modifier.height(TextFieldDefaults.MinHeight * 2),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                singleLine = false
            ) {
                planText = it
            }
        }
    }

    if (showPlanDatePicker) {
        val confirmEnabled by remember {
            derivedStateOf { planeDatePicker.selectedDateMillis != null }
        }
        val colors = DatePickerDefaults.colors(
            containerColor = match2,
            titleContentColor = match1,
            headlineContentColor = match1,
            weekdayContentColor = match1,
            navigationContentColor = match1,
            yearContentColor = match1,
            disabledYearContentColor = match1.copy(0.5f),
            selectedYearContentColor = match2,
            selectedYearContainerColor = match1,
            subheadContentColor = match1,
            dayContentColor = match1,
            selectedDayContentColor = match2,
            selectedDayContainerColor = match1,
            todayContentColor = match1,
            todayDateBorderColor = match1,
            dividerColor = match1
        )
        DatePickerDialog(
            modifier = Modifier.padding(10.dp),
            onDismissRequest = { showPlanDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPlanDatePicker = false
                        date = planeDatePicker.selectedDateMillis!!.toDateString()
                    },
                    enabled = confirmEnabled
                ) {
                    DefaultText(
                        text = "확인",
                        color = match1,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPlanDatePicker = false
                    },
                ) {
                    DefaultText(
                        text = "취소",
                        color = match1,
                    )
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            ),
            colors = colors
        )
        {
            DatePicker(
                state = planeDatePicker,
                colors = colors,
                showModeToggle = false
            )
        }
    }

}

@Composable
@Preview
fun PlanDialogPreView() {
    PlanDialog("2023.05.12", "2023.05.14", Plan(0, 0))
}