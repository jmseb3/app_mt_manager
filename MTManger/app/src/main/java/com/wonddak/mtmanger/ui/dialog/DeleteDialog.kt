package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogDeleteBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText

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