package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import android.widget.Toast
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogCategoryBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog

class CategoryDialog(
    private val categoryDialogCallback: CategoryDialogCallback
) : BaseDialog<DialogCategoryBinding>(R.layout.dialog_category) {

    companion object {
        fun newInstance(
            name: String,
            callback: CategoryDialogCallback
        ): CategoryDialog =
            CategoryDialog(callback).apply {
                arguments = Bundle().apply {
                    putString(Const.Dialog.Name, name)

                }
            }
    }

    interface CategoryDialogCallback {
        fun onClick(item: String)

    }

    override fun initBinding() {
        arguments?.let {
            it.getString(Const.Dialog.Name).let { name ->
                binding.itemCategoyEdit.setText(name)
            }
        }
        binding.apply {
            cancel.setOnClickListener {
                dismiss()
            }

            ok.setOnClickListener {
                val categoryTemp = binding.itemCategoyEdit.text.toString()

                if (categoryTemp.isEmpty()) {
                    Toast.makeText(requireContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    categoryDialogCallback.onClick(categoryTemp)
                    dismiss()
                }
            }


        }

    }
}