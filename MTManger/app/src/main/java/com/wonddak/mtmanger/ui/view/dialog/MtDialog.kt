package com.wonddak.mtmanger.ui.view.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.toDateString
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MTDialog(
    mtData: MtData? = null,
    onDismiss: () -> Unit = {},
    onAdd: (
        title: String,
        fee: String,
        startDate: String,
        endDate: String
    ) -> Unit
) {

    var title by remember {
        mutableStateOf(mtData?.mtTitle ?: "")
    }

    var fee by remember {
        mutableStateOf(mtData?.fee?.toString() ?: "")
    }

    var startDate by remember {
        mutableStateOf(mtData?.mtStart ?: "")
    }

    var endDate by remember {
        mutableStateOf(mtData?.mtStart ?: "")
    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDateRangePicker by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDateRangePickerState()

    DialogBase(
        titleText = if (mtData == null) "MT 정보 추가" else " MT 정보 수정",
        confirmText = if (mtData == null) "추가" else "수정",
        onConfirm = {
            if (title.isEmpty() || fee.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.dialog_error_field),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onAdd(title, fee, startDate, endDate)
            }
        },
        onDismiss = onDismiss
    ) {
        Column() {
            DialogTextField(
                value = if (startDate.isNotEmpty() && endDate.isNotEmpty()) "$startDate ~ $endDate" else "",
                placeHolder = "일자를 선택해 주세요",
                label = "MT 일자",
                change = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDateRangePicker = true
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
                placeHolder = "제목을 입력해주세요",
                label = "MT 제목",
                change = { title = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = fee,
                placeHolder = "회비을 입력해주세요",
                label = "MT 회비",
                change = { fee = it.replace("\\D".toRegex(), "") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_won),
                        contentDescription = null,
                        tint = match1,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }

    if (showDateRangePicker) {
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis != null }
        }
        val colors = DatePickerDefaults.colors(
            containerColor = match2,
            titleContentColor = match1,
            weekdayContentColor = match1,
            subheadContentColor = match1,
            headlineContentColor = match1,
            dayContentColor = match1,
            selectedDayContainerColor = match1,
            selectedDayContentColor = match2,
            todayContentColor = match1,
            todayDateBorderColor = match1,
            dayInSelectionRangeContentColor = match2.copy(0.5f),
            dayInSelectionRangeContainerColor = match1.copy(0.5f),
            dividerColor = match1
        )
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDateRangePicker = false
                        startDate = datePickerState.selectedStartDateMillis!!.toDateString()
                        endDate = datePickerState.selectedEndDateMillis!!.toDateString()
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
                        showDateRangePicker = false
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
            DateRangePicker(
                modifier = Modifier.fillMaxHeight(0.9f),
                state = datePickerState,
                title = {

                },
                headline = {
                    DefaultText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        text = "MT 일자 선택",
                        color = match1,
                    )
                },
                colors = colors,
                showModeToggle = false
            )
        }
    }

}