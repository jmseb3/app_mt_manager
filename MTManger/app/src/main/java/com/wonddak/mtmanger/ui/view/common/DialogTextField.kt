package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeHolder: String,
    label: String,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    change: (value: String) -> Unit
) {
    val color = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = match1,
        textColor = match1,
        disabledBorderColor = match1,
        disabledTextColor = match1,
        cursorColor = match1,
        unfocusedBorderColor = match1.copy(alpha = 0.5f)
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { change(it) },
        placeholder = {
            DefaultText(
                text = placeHolder,
                color = match1.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
        },
        enabled = enabled,
        readOnly = readOnly,
        label = {
            DefaultText(
                text = label,
                color = match1,
                fontSize = 13.sp
            )
        },
        colors = color,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = TextStyle(
            fontFamily = maple
        ),
        singleLine = singleLine
    )
}