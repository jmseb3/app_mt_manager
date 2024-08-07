package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1


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
    color : Color = match1,
    change: (value: String) -> Unit
) {
    val colorSetting = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = color,
        focusedTextColor = color,
        unfocusedTextColor = color,
        disabledBorderColor = color,
        disabledTextColor = color,
        cursorColor = color,
        unfocusedBorderColor = color.copy(alpha = 0.5f)
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { change(it) },
        placeholder = {
            DefaultText(
                text = placeHolder,
                color = color.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
        },
        enabled = enabled,
        readOnly = readOnly,
        label = {
            DefaultText(
                text = label,
                color = color,
                fontSize = 13.sp
            )
        },
        colors = colorSetting,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = TextStyle(
            fontFamily = maple()
        ),
        singleLine = singleLine
    )
}