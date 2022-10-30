package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogPlanBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog
import java.text.SimpleDateFormat

class PlanDialog(
    private val planDialogCallback: PlanDialogCallback
) : BaseDialog<DialogPlanBinding>(R.layout.dialog_plan) {

    interface PlanDialogCallback {
        fun onClick(
            title: String,
            day: String,
            text: String
        )

        fun dateBtnClick(editText: EditText)
    }

    companion object {
        fun newInstance(
            callback: PlanDialogCallback,
            title: String,
            day: String,
            text: String
        ): PlanDialog =
            PlanDialog(callback).apply {
                arguments = Bundle().apply {
                    putString(Const.Dialog.Title, title)
                    putString(Const.Dialog.Day, day)
                    putString(Const.Dialog.Text, text)
                }
            }
    }

    override fun initBinding() {
        arguments?.let {
            val title = it.getString(Const.Dialog.Title)
            val day = it.getString(Const.Dialog.Day)
            val text = it.getString(Const.Dialog.Text)
            var flag = true
            if (title == getString(R.string.plan_dialog_default_text)) {
                flag = false
            }
            if (day == getString(R.string.plan_dialog_default_day)) {
                flag = false
            }
            if (text == getString(R.string.plan_dialog_default_text)) {
                flag = false
            }
            if (flag) {
                binding.dialogDate.setText(day)
                binding.dialogPlanDo.setText(text)
                binding.dialogPlanTitle.setText(title)
            }

        }
        binding.apply {
            cancel.setOnClickListener {
                dismiss()
            }
            dialogDateBtn.setOnClickListener {
                planDialogCallback.dateBtnClick(dialogDate)
            }

            ok.setOnClickListener {
                val titleTemp = dialogPlanTitle.text.toString()
                val dayTemp = dialogDate.text.toString()
                val textTemp = dialogPlanDo.text.toString()
                if (titleTemp.isEmpty() || dayTemp.isEmpty() || textTemp.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.dialog_error_field),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    planDialogCallback.onClick(
                        titleTemp,
                        dayTemp,
                        textTemp
                    )
                    dismiss()
                }
            }

        }

    }
}