package com.wonddak.mtmanger.ui.view.dialog

import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.dialog.DatePicker
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
                enabled = false,
                change = {},
                modifier = Modifier
                    .noRippleClickable() {
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
                )
            ) {
                planText = it
            }
        }
    }
}

@Composable
@Preview
fun PlanDialogPreView() {
    PlanDialog("2023.05.12", "2023.05.14", Plan(0, 0))
}