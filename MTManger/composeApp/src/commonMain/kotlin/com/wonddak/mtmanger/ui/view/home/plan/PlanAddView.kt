package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.room.entity.PlanData
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.ByteArrayImageView
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import com.wonddak.mtmanger.ui.view.dialog.OneDatePickerDialog
import com.wonddak.mtmanger.util.rememberPhotoPickerLauncher
import kotlinx.datetime.Instant
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.add_photo
import mtmanger.composeapp.generated.resources.calendar
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanAddView(
    startDate: String,
    endDate: String,
    plan: Plan? = null,
    onAdd: (PlanData) -> Unit,
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var date by remember {
        mutableStateOf(plan?.nowDay ?: "")
    }
    var title by remember {
        mutableStateOf(plan?.nowPlanTitle ?: "")
    }
    var planText by remember {
        mutableStateOf(plan?.simpleText ?: "")
    }
    var link by remember {
        mutableStateOf(plan?.link ?: "")
    }
    var imageByte: ByteArray? by remember {
        mutableStateOf(plan?.imgBytes)
    }
    val photoPickerLauncher = rememberPhotoPickerLauncher(
        onResult = {
            imageByte = it
        }
    )
    val textFiledModifier = Modifier.fillMaxWidth()
    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    val enableLink = if (link.isEmpty()) {
        true
    } else {
        val urlRegex =
            """(http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?""".toRegex()
        urlRegex.matches(link)
    }
    val enabled = date.isNotEmpty()
            && title.isNotEmpty()
            && planText.isNotEmpty()
            && enableLink
    val color = if (enabled) match2 else match2.copy(0.5f)

    Box(
        Modifier
            .fillMaxSize()
            .noRippleClickable {
                focusManager.clearFocus()
            }
            .padding(10.dp)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if(isImeVisible) 10.dp else 60.dp, start = 10.dp, end = 10.dp, top = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DialogTextField(
                value = date,
                placeHolder = "일자를 선택해 주세요",
                label = "일자",
                enabled = false,
                change = {},
                modifier = textFiledModifier,
                color = match2,
                trailingIcon = {
                    if (date.isEmpty()) {
                        IconButton(
                            onClick = {
                                showDatePicker = true
                            },
                            modifier = Modifier
                        ) {
                            Icon(
                                painterResource(Res.drawable.calendar),
                                null,
                                tint = match2
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                date = ""
                            },
                            modifier = Modifier
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                null,
                                tint = match2
                            )
                        }
                    }
                }
            )

            DialogTextField(
                value = title,
                placeHolder = "제목을 입력해 주세요",
                label = "제목",
                modifier = textFiledModifier,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                color = match2
            ) {
                title = it
            }
            DialogTextField(
                value = link,
                placeHolder = "추가할 url을 입력해 주세요(선택)",
                label = "링크",
                modifier = textFiledModifier,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Uri
                ),
                color = match2
            ) {
                link = it
            }

            DialogTextField(
                value = planText,
                placeHolder = "계획을 입력해 주세요",
                label = "계획",
                modifier = Modifier.height(TextFieldDefaults.MinHeight * 2).then(textFiledModifier),
                singleLine = false,
                color = match2
            ) {
                planText = it
            }
            Row {
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch()
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(resource = Res.drawable.add_photo),
                        contentDescription = null,
                        tint = match2
                    )
                }
                if (imageByte != null) {
                    IconButton(
                        onClick = {
                            imageByte = null
                        },
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = match2
                        )
                    }
                }
            }
            imageByte?.let {
                ByteArrayImageView(Modifier.fillMaxWidth().wrapContentHeight(), it)
            }
        }
        if (!isImeVisible) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.BottomCenter),
                onClick = {
                    onAdd(
                        PlanData(
                            nowDay = date,
                            nowPlanTitle = title,
                            simpleText = planText,
                            link = link,
                            imgBytes = imageByte
                        )
                    )
                },
                enabled = enabled,
                border = BorderStroke(2.dp, color),
            ) {
                DefaultText(text = plan?.let { "수정" } ?: "추가", color = color)
            }
        }
    }


    if (showDatePicker) {
        val start = Instant.parse(
            startDate.replace(
                ".",
                "-"
            ) + "T00:00:00Z"
        ).toEpochMilliseconds()
        val end = Instant.parse(
            endDate.replace(
                ".",
                "-"
            ) + "T00:00:00Z"
        ).toEpochMilliseconds()
        OneDatePickerDialog(
            initialSelectedDateMillis = start,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in (start..end)
                }
            },
            onDismiss = {
                showDatePicker = false
            }
        ) {
            date = it
        }
    }
}