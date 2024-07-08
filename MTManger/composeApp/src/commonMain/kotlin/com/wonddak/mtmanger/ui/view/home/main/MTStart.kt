package com.wonddak.mtmanger.ui.view.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.toDateString
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_won
import org.jetbrains.compose.resources.painterResource

@Composable
fun MtStart() {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scroll).fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Top
    ) {
        editMtData()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editMtData() {
    val keyboardController = LocalSoftwareKeyboardController.current

    var title by remember {
        mutableStateOf("")
    }

    var fee by remember {
        mutableStateOf( "")
    }

    var startDate by remember {
        mutableStateOf("")
    }

    var endDate by remember {
        mutableStateOf("")
    }
    val datePickerState = rememberDateRangePickerState()
    Column(
        modifier = Modifier
            .background(match2)
            .clip(RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        DialogTextField(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            value = title,
            placeHolder = "제목을 입력해주세요",
            label = "MT 제목",
            change = { title = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )
        DialogTextField(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            value = fee,
            placeHolder = "회비을 입력해주세요",
            label = "MT 회비",
            change = { fee =  it.replace("\\D".toRegex(), "") },
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
                    painter = painterResource(resource = Res.drawable.ic_won),
                    contentDescription = null,
                    tint = match1,
                    modifier = Modifier.size(18.dp)
                )
            },
        )
        if (title.isNotEmpty() && fee.isNotEmpty()) {
            DateRangePicker(
                modifier = Modifier.height(300.dp),
                state = datePickerState,
                dateFormatter = remember {
                    DatePickerDefaults.dateFormatter(
                        "yy MM dd",
                        "yy MM dd",
                        "yy MM dd"
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
                        Box(Modifier.weight(0.2f)) {
                            Text("~")
                        }
                        Box(Modifier.weight(1f)) {
                            Text(datePickerState.selectedEndDateMillis?.let {
                                it.toDateString()
                            } ?: "End Date")
                        }
                        Box(Modifier.weight(0.2f)) {
                            IconButton(
                                onClick = {
                                    startDate = datePickerState.selectedStartDateMillis.toDateString()
                                    endDate = datePickerState.selectedEndDateMillis.toDateString()
                                },
                                enabled = datePickerState.selectedStartDateMillis != null && datePickerState.selectedEndDateMillis != null
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Ok"
                                )
                            }
                        }
                    }
                },
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = match2,
                    titleContentColor = match1,
                    headlineContentColor = match1,
                    weekdayContentColor = match1,
                    yearContentColor = match1,
                    dayInSelectionRangeContainerColor = match1.copy(alpha = 0.5f),
                    dayInSelectionRangeContentColor = match2,
                    selectedDayContainerColor = match1,
                    todayDateBorderColor = match1,
                    todayContentColor = match1.copy(alpha = 0.5f),
                    dividerColor = match1
                )
            )
        }
    }
}