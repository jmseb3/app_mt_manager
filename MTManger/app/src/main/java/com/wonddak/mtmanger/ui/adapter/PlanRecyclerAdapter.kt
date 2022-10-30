package com.wonddak.mtmanger.ui.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wonddak.mtmanger.AddDialog
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.databinding.ItemPlanBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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