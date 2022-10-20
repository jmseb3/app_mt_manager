package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemMtlistBinding
import com.wonddak.mtmanger.room.MtData

class MtListRecyclerAdapter : BaseDataBindingRecyclerAdapter<MtData, ItemMtlistBinding>(R.layout.item_mtlist) {

    interface MtListItemCallback{
        fun itemClick(mtData: MtData)
    }
    private var mtListItemCallback :MtListItemCallback? = null

    fun setCallBack(listener:MtListItemCallback){
        mtListItemCallback = listener
    }
    override fun onBindItem(binding: ItemMtlistBinding, item: MtData) {
        binding.mtData = item
        binding.itemMtall.setOnClickListener {
            mtListItemCallback?.itemClick(item)
        }
    }
}