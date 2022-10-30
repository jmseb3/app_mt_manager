package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemBuylistBinding
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter

class BuyRecyclerAdapter(
    private val buyListItemCallback: BuyListItemCallback
) : BaseDataBindingRecyclerAdapter<BuyGood,ItemBuylistBinding>(R.layout.item_buylist) {

    interface BuyListItemCallback {
        fun itemClick(item: BuyGood)
        fun itemLongClick(item: BuyGood)
    }

    override fun onBindItem(binding: ItemBuylistBinding, item: BuyGood) {
        binding.buyGood = item
        binding.itemBuyall.apply {
            setOnClickListener {
                buyListItemCallback.itemClick(item)
            }
            setOnLongClickListener {
                buyListItemCallback.itemLongClick(item)
                return@setOnLongClickListener true
            }
        }
    }
}