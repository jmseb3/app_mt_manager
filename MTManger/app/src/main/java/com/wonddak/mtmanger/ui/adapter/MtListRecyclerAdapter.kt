package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemMtlistBinding
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter

class MtListRecyclerAdapter(
    private val mtListItemCallback: MtListItemCallback
) : BaseDataBindingRecyclerAdapter<MtData, ItemMtlistBinding>(R.layout.item_mtlist) {

    interface MtListItemCallback{
        fun itemClick(mtData: MtData)
    }
    override fun onBindItem(binding: ItemMtlistBinding, item: MtData) {
        binding.mtData = item
        binding.itemMtall.setOnClickListener {
            mtListItemCallback.itemClick(item)
        }
    }
}