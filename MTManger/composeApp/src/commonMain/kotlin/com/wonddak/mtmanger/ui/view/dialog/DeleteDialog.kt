package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteDialog(
    msg: String = "정말 삭제하시겠습니까?",
    onDelete:() ->Unit = {},
    onDismiss:() -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = msg,
                color = match1,
                fontFamily = maple(),
                fontSize = 17.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                DefaultText(
                    text ="삭제",
                    color = match1,
                )
            }

        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                DefaultText(
                    text =  "취소",
                    color = match1,
                )
            }
        },
        containerColor = match2
    )
}

@Composable
@Preview
fun DeleteDialogPreView(){
    DeleteDialog(msg = "정말 삭제하시겠습니까?")
}