package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogDeleteBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2

class DeleteDialog(
    private val deleteDialogCallback: DeleteDialogCallback
) : BaseDialog<DialogDeleteBinding>(R.layout.dialog_delete) {

    interface DeleteDialogCallback {
        fun onclick()
    }

    companion object {
        fun newInstance(
            callback: DeleteDialogCallback,
            title: String? = null
        ): DeleteDialog =
            DeleteDialog(callback).apply {
                arguments = Bundle().apply {
                    putString(Const.Dialog.Title, title)
                }
            }
    }

    private var mainText: String = "정말 삭제하시겠습니까?"
    override fun initBinding() {
        arguments?.let {
            mainText = it.getString(Const.Dialog.Title) ?: "정말 삭제하시겠습니까?"
        }
        binding.body = mainText

        binding.apply {
            personok.setOnClickListener {
                deleteDialogCallback.onclick()
                dismiss()
            }
            personcancel.setOnClickListener {
                dismiss()
            }
        }
    }
}

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
                fontFamily = maple,
                fontSize = 17.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(
                    "삭제",
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
fun DeleteDialogPreView(){
    DeleteDialog(msg = "정말 삭제하시겠습니까?")
}