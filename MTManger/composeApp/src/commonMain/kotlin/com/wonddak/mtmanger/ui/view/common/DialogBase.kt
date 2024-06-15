package com.wonddak.mtmanger.ui.view.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2

@Composable
fun DialogBase(
    titleText: String = "",
    confirmText: String = "확인",
    confirmEnabled: Boolean = true,
    onConfirm: () -> Unit,
    dismissText: String = "취소",
    dismissEnabled: Boolean = true,
    onDismiss: () -> Unit,
    bodyContent: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (titleText.isNotEmpty()) {
                DefaultText(
                    text = titleText,
                    color = match1,
                    fontSize = 17.sp,
                )
            }
        },
        text = {
            bodyContent()
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = confirmEnabled
            ) {
                DefaultText(
                    text = confirmText,
                    color = if(confirmEnabled) match1 else match1.copy(alpha = 0.5f),
                )
            }

        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = dismissEnabled
            ) {
                DefaultText(
                    text = dismissText,
                    color = match1,
                )
            }
        },
        containerColor = match2
    )
}