package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.room.SimplePerson
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_baseline_contact_phone_24
import mtmanger.composeapp.generated.resources.ic_won
import org.jetbrains.compose.resources.painterResource

@Composable
fun PersonDialog(
    person: Person?,
    onAdd: (person: SimplePerson) -> Unit = { _ -> },
    onDismiss: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    var personData: SimplePerson by remember {
        mutableStateOf(person?.toSimplePerson() ?: SimplePerson())
    }

    DialogBase(
        titleText = if (person != null) "인원 수정" else "인원 추가",
        confirmText = if (person != null) "수정" else "추가",
        onConfirm = { onAdd(personData) },
        confirmEnabled = personData.isConfirm(),
        onDismiss = onDismiss
    ) {
        Column() {
            if (person == null) {
                ContactButton(
                    modifier = Modifier,
                    updateValue = {
                        personData = it
                        focusRequester.requestFocus()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultText(
                            text = "연락처에서 불러오기",
                            color = match1
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(resource = Res.drawable.ic_baseline_contact_phone_24),
                            contentDescription = null,
                            tint = match1
                        )
                    }
                }
            }

            DialogTextField(
                value = personData.name,
                placeHolder = "이름 입력해 주세요",
                label = "이름",
                change = { personData = personData.copy(name = it) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = personData.phoneNumber,
                placeHolder = "전화번호를 입력하세요",
                label = "전화번호",
                change = {
                    personData = personData.copy(phoneNumber = it.replace("\\D".toRegex(), ""))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = personData.paymentFee,
                placeHolder = "납부금액을 입력해 주세요",
                label = "납부금액",
                change = {
                    personData = personData.copy(paymentFee = it.replace("\\D".toRegex(), ""))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.focusRequester(focusRequester),
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
