package com.wonddak.mtmanger.ui.dialog

import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDialog(
    startDate: String,
    endDate: String,
    plan:Plan?,
    onAdd: (
        title: String,
        day: String,
        text: String
    ) -> Unit = { _, _, _ ->},
    onDismiss: () -> Unit = {}
) {

    var title by remember {
        mutableStateOf(plan?.nowplantitle ?: "")
    }
    var date by remember {
        mutableStateOf(plan?.nowday?: "")
    }
    val context = LocalContext.current

    var planText by remember {
        mutableStateOf(plan?.simpletext?: "")
    }
    val color = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = match1,
        textColor = match1,
        disabledBorderColor = match1,
        disabledTextColor = match1,
        cursorColor = match1
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (plan != null) "계획 수정" else "계획 작성",
                color = match1,
                fontFamily = maple,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(text = "제목을 입력해 주세요")
                    },
                    label = {
                        Text(
                            text = "제목",
                            color = match1,
                            fontFamily = maple
                        )
                    },
                    colors = color
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    modifier = Modifier
                        .clickable {
                            val minDate = Calendar.getInstance()
                            minDate.set(
                                startDate.split(".")[0].toInt(),
                                startDate.split(".")[1].toInt() - 1,
                                startDate.split(".")[2].toInt()
                            )
                            val maxDate = Calendar.getInstance()
                            maxDate.set(
                                endDate.split(".")[0].toInt(),
                                endDate.split(".")[1].toInt() - 1,
                                endDate.split(".")[2].toInt()
                            )
                            DatePicker.show(
                                context,
                                minDate.time.time,
                                maxDate.time.time
                            ) { _, year, month, day ->
                                date = "$year.${month + 1}.$day"
                            }
                        },
                    enabled = false,
                    placeholder = {
                        Text(text = "일자를 선택해 주세요")
                    },
                    label = {
                        Text(
                            text = "일자",
                            color = match1,
                            fontFamily = maple
                        )
                    },
                    colors = color
                )
                OutlinedTextField(
                    modifier = Modifier.height(TextFieldDefaults.MinHeight * 2),
                    value = planText,
                    onValueChange = { planText = it },
                    placeholder = {
                        Text(text = "계획을 입력해 주세요")
                    },
                    label = {
                        Text(
                            text = "계획",
                            color = match1,
                            fontFamily = maple
                        )
                    },
                    colors = color
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isEmpty() || date.isEmpty() || planText.isEmpty()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.dialog_error_field),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onAdd(title,date,planText)
                }
            }) {
                Text(
                    if (plan != null) "수정" else "추가",
                    color = match1,
                    fontFamily = maple
                )
            }

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "취소",
                    color = match1,
                    fontFamily = maple
                )
            }
        },
        containerColor = match2
    )
}

@Composable
@Preview
fun PlanDialogPreView() {
    PlanDialog("2023.05.12", "2023.05.14", Plan(0,0))
}