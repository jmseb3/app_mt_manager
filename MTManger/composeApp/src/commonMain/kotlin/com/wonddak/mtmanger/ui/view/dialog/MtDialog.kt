package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_won
import org.jetbrains.compose.resources.painterResource

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

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    DialogBase(
        titleText = if (mtData == null) "MT 정보 추가" else " MT 정보 수정",
        confirmText = if (mtData == null) "추가" else "수정",
        onConfirm = {
            if (title.isEmpty() || fee.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.dialog_error_field),
//                    Toast.LENGTH_SHORT
//                ).show()
            } else {
                onAdd(title, fee, startDate, endDate)
            }
        },
        onDismiss = onDismiss
    ) {
        Column() {
            DialogTextField(
                value = startDate,
                placeHolder = "일자를 선택해 주세요",
                label = "MT 시작일",
                enabled = false,
                change = {},
                modifier = Modifier
                    .noRippleClickable() {
                       // 일자 선택 출력
                    }
            )
            DialogTextField(
                value = endDate,
                placeHolder = "일자를 선택해 주세요",
                label = "MT 종료일",
                enabled = false,
                change = {},
                modifier = Modifier
                    .noRippleClickable() {
                        if(startDate.isNotEmpty()) {
                            // 일자 선택 출력
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
                ), keyboardActions = KeyboardActions(
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
                }
            )
        }
    }

}