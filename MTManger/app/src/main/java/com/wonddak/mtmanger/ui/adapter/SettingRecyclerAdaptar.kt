package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemCategoryBinding
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter


class SettingRecyclerAdapter(
    private val settingCallback: SettingCallback
) :
    BaseDataBindingRecyclerAdapter<categoryList, ItemCategoryBinding>(R.layout.item_category) {

    interface SettingCallback {
        fun onClick(item: categoryList)
        fun onLongClick(item: categoryList)
    }

    override fun onBindItem(binding: ItemCategoryBinding, item: categoryList) {
        binding.name = item.name

        binding.itemCategory.apply {
            setOnClickListener {
                settingCallback.onClick(item)
            }
            setOnLongClickListener {
                settingCallback.onLongClick(item)
                return@setOnLongClickListener true
            }
        }
    }
}