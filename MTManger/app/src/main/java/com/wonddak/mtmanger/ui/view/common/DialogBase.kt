package com.wonddak.mtmanger.ui.view.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2

@Composable
fun DialogBase(
    modifier: Modifier = Modifier,
    titleText: String = "",
    confirmText: String = "확인",
    onConfirm: () -> Unit,
    dismissText: String = "취소",
    onDismiss: () -> Unit,
    bodyContent: @Composable () -> Unit
) {
    AlertDialog(
        modifier = modifier,
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
            TextButton(onClick = onConfirm) {
                DefaultText(
                    text = confirmText,
                    color = match1,
                )
            }

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                DefaultText(
                    text = dismissText,
                    color = match1,
                )
            }
        },
        containerColor = match2
    )
}