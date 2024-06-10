package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        mutableStateOf(plan?.nowPlanTitle ?: "")
    }
    var date by remember {
        mutableStateOf(plan?.nowDay ?: "")
    }
    var planText by remember {
        mutableStateOf(plan?.simpleText ?: "")
    }
    val focusManager = LocalFocusManager.current

    DialogBase(
        titleText = if (plan != null) "계획 수정" else "계획 작성",
        confirmText = if (plan != null) "수정" else "추가",
        onConfirm = {
            if (title.isEmpty() || date.isEmpty() || planText.isEmpty()) {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.dialog_error_field),
//                    Toast.LENGTH_SHORT
//                ).show()
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
                       //일정 수정
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
}