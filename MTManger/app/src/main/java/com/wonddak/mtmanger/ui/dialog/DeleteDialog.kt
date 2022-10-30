package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogDeleteBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog

class DeleteDialog(
    private val deleteDialogCallback: DeleteDialogCallback
) : BaseDialog<DialogDeleteBinding>(R.layout.dialog_delete) {

    interface DeleteDialogCallback {
        fun onclick()
    }

    companion object {
        fun newInstance(
            callback: DeleteDialogCallback,
            title: String?=null
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