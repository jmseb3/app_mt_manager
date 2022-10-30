package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogPersonBinding
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog

class PersonDialog(
    private val personDialogOkCallBack: PersonDialogOkCallBack
) : BaseDialog<DialogPersonBinding>(R.layout.dialog_person) {

    companion object {
        fun newInstance(
            callBack: PersonDialogOkCallBack,
            name: String? = null,
            number: String? = null,
            fee: String? = null
        ): PersonDialog {
            return PersonDialog(callBack).apply {
                arguments = Bundle().apply {
                    putString(Const.Dialog.Name, name)
                    putString(Const.Dialog.Number, number)
                    putString(Const.Dialog.FEE, fee)
                }
            }
        }
    }

    private var newName: String? = null
    private var newNumber: String? = null
    private var newFee: String? = null
    override fun initBinding() {
        arguments?.let {
            newFee = it.getString(Const.Dialog.FEE)
            newName = it.getString(Const.Dialog.Name)
            newNumber = it.getString(Const.Dialog.Number)
        }


        binding.apply {
            newName?.let {
                addpersonName.setText(it)
            }
            newNumber?.let {
                addmpersonNumber.setText(it)
            }

            if (newFee != null) {
                addpersonFromphonedata.visibility = View.GONE
                mtdialogtitle.text = "정보 수정"
                personok.text = "수정"
                addmpersonFee.setText(newFee)
            }

            personcancel.setOnClickListener {
                dismiss()
            }
            addpersonFromphonedata.setOnClickListener {
                personDialogOkCallBack.addPersonFromPhoneData()
                dismiss()
            }
            personok.setOnClickListener {
                val tempName = addpersonName.text.toString()
                val tempFee = addmpersonFee.text.toString()
                val tempNumber = addmpersonNumber.text.toString()

                if (tempName.isEmpty() || tempFee.isEmpty() || tempNumber.isEmpty()) {
                    Toast.makeText(requireContext(), "모든 항목을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    personDialogOkCallBack.onClick(
                        tempName,
                        tempFee,
                        tempNumber
                    )
                    dismiss()
                }
            }
        }

    }

    interface PersonDialogOkCallBack {
        fun addPersonFromPhoneData() {}
        fun onClick(
            name: String,
            fee: String,
            number: String
        )

    }
}