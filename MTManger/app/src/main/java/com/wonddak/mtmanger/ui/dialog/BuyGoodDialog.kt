package com.wonddak.mtmanger.ui.dialog

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.DialogBuyBinding
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.common.dialog.BaseDialog

class BuyGoodDialog(
    private val buyGoodDialogCallback: BuyGoodDialogCallback,
    private val categoryList : List<categoryList>,
) : BaseDialog<DialogBuyBinding>(R.layout.dialog_buy) {

    companion object {
        fun newInstance(
            callback: BuyGoodDialogCallback,
            categoryList: List<categoryList>,
            count: String? = null,
            price: String? = null,
            name: String? = null
        ): BuyGoodDialog =
            BuyGoodDialog(callback,categoryList).apply {
                arguments = Bundle().apply {
                    putString(Const.Dialog.Name, name)
                    putString(Const.Dialog.Count, count)
                    putString(Const.Dialog.Price, price)

                }
            }
    }

    interface BuyGoodDialogCallback {
        fun onClick(
            category :String,
            name : String,
            count : String,
            price: String
        )

    }

    private var count: String? = null
    private var price: String? = null
    private var name: String? = null
    override fun initBinding() {
        arguments?.let {
            count = it.getString(Const.Dialog.Count)
            price = it.getString(Const.Dialog.Price)
            name = it.getString(Const.Dialog.Name)
        }
        binding.addBuyCount.setText(0.toString())
        if (count != null && price != null && name != null) {
            binding.apply {
                addBuyPrice.setText(price)
                addBuyCount.setText(count)
                addBuyName.setText(name)
            }
        }
        binding.apply {
            addBuyCountPlus.setOnClickListener {
                val countTemp = addBuyCount.text
                addBuyCount.setText((countTemp.toString().toInt() + 1).toString())
            }
            addBuyCountMinus.setOnClickListener {
                val countTemp = addBuyCount.text
                if (countTemp.toString() == 0.toString()) {
                    addBuyCount.setText(0.toString())
                } else {
                    addBuyCount.setText((countTemp.toString().toInt() - 1).toString())
                }
            }
            cancel.setOnClickListener {
                dismiss()
            }
            addBuySpiner.apply {
                adapter = ArrayAdapter(requireContext(), R.layout.spinner_custom, categoryList.map { it.name })
                onItemSelectedListener
            }
            ok.setOnClickListener {
                val categoryTemp = binding.addBuySpiner.selectedItem.toString()
                val nameTemp = binding.addBuyName.text.toString()
                val countTemp = binding.addBuyCount.text.toString()
                val priceTemp = binding.addBuyPrice.text.toString()

                if (categoryTemp.isEmpty() || nameTemp.isEmpty() || countTemp.isEmpty() || priceTemp.isEmpty()) {
                    Toast.makeText(requireContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                }else {
                    buyGoodDialogCallback.onClick(categoryTemp,nameTemp,countTemp,priceTemp)
                    dismiss()
                }
            }
        }
    }
}