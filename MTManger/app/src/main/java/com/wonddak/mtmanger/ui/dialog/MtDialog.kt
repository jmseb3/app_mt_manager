package com.wonddak.mtmanger.ui.dialog

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.fragment.app.activityViewModels
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.DialogAddmtdataBinding
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.viewModel.MTViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class MtDialog : BaseDialog<DialogAddmtdataBinding>(R.layout.dialog_addmtdata) {

    companion object {
        const val Dialog_Title = "dialog_title"
        const val Dialog_FEE = "dialog_fee"
        const val Dialog_Start = "dialog_start"
        const val Dialog_End = "dialog_end"

        fun newInstance(
            title: String? = null,
            fee: Int? = null,
            start: String? = null,
            end: String? = null
        ): MtDialog {
            return MtDialog().apply {
                arguments = Bundle().apply {
                    putString(Dialog_Title, title)
                    putString(Dialog_FEE, fee.toString())
                    putString(Dialog_Start, start)
                    putString(Dialog_End, end)
                }
            }

        }
    }

    @Inject
    lateinit var preferences: SharedPreferences

    interface MtDialogOkCallBack {
        fun onClick(
            title: String,
            fee: Int,
            startDate: String,
            endDate: String
        )
    }

    private var mtDialogOkCallBack: MtDialogOkCallBack? = null

    fun setMtDialogCallback(callback: MtDialogOkCallBack) {
        mtDialogOkCallBack = callback
    }

    private var newTitle: String? = null
    private var newFee: String? = null
    private var newStart: String? = null
    private var newEnd: String? = null
    override fun initBinding() {
        arguments?.let {
            newTitle = it.getString(Dialog_Title)
            newFee = it.getString(Dialog_Title)
            newStart = it.getString(Dialog_Start)
            newEnd = it.getString(Dialog_End)
        }
        val editor = preferences.edit()

        binding.apply {
            if (!newTitle.isNullOrEmpty() && !newFee.isNullOrEmpty() && !newStart.isNullOrEmpty() && !newEnd.isNullOrEmpty()) {
                mtdialogtitle.text = "MT 정보 수정"
                ok.text = "수정"

                mMainScope.launch {
                    addmtdataTitle.setText(newTitle)
                    addmtdataStart.setText(newStart)
                    addmtdataEnd.setText(newEnd)
                    addmtdataFee.setText(newFee)
                }
            }
            addmtdataStartSelect.setOnClickListener {
                DatePicker.show(
                    requireContext()
                ) { _, year, month, day ->
                    val tempDate = "${year}.${month + 1}.${day}"
                    binding.addmtdataStart.setText(tempDate)
                }
            }

            addmtdataEndSelect.setOnClickListener {
                addmtdataStart.text.toString().let { startTempDate ->
                    if (startTempDate.isEmpty()) {
                        Toast.makeText(requireContext(), "시작일을 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        DatePicker.show(
                            requireContext()
                        ) { _, year, month, day ->
                            val tempDate = "${year}.${month + 1}.${day}"
                            val transFormat = SimpleDateFormat("yyyy.MM.dd")
                            val startDate = transFormat.parse(startTempDate)
                            val endDate = transFormat.parse(tempDate)
                            if (startDate <= endDate) {
                                binding.addmtdataEnd.setText(tempDate)
                            } else {
                                Toast.makeText(context, "종료일을 다시 설정해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            ok.setOnClickListener {
                val titleTemp = addmtdataTitle.text.toString()
                val startTemp = addmtdataStart.text.toString()
                val endTemp = addmtdataEnd.text.toString()
                val feeTemp = addmtdataFee.text.toString()

                if (titleTemp.isEmpty() || startTemp.isEmpty() || endTemp.isEmpty() || feeTemp.isEmpty()) {
                    Toast.makeText(requireContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    mtDialogOkCallBack?.onClick(
                        titleTemp,
                        feeTemp.toInt(),
                        startTemp,
                        endTemp
                    )
                    dismiss()
                }

            }

            cancel.setOnClickListener {
                dismiss()
            }
        }

    }
}