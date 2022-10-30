package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemPlanBinding
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter

class PlanRecyclerAdapter(
    private val planListItemCallback: PlanListItemCallback
) : BaseDataBindingRecyclerAdapter<Plan, ItemPlanBinding>(R.layout.item_plan) {

    interface PlanListItemCallback {
        fun itemClick(item: Plan)
        fun itemLongClick(item: Plan)
        fun itemAddPhoto(item: Plan)
        fun itemImgLongClick(item: Plan)
    }

    override fun onBindItem(binding: ItemPlanBinding, item: Plan) {
        binding.plan = item

        binding.itemAddPhoto.setOnClickListener {
            planListItemCallback.itemAddPhoto(item)
        }

        binding.itemPlanImg.setOnLongClickListener {
            planListItemCallback.itemImgLongClick(item)
            return@setOnLongClickListener true
        }

        binding.apply {
            listOf(itemPlanTitle,itemPlanDo,itemPlanCardAll,itemPlanDate).forEach{
                it.setOnClickListener {
                    planListItemCallback.itemClick(item)
                }
                it.setOnLongClickListener {
                    planListItemCallback.itemLongClick(item)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}