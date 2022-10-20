package com.wonddak.mtmanger.ui.common.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * BaseViewHolder
 * @param ITEM list의 들어가는 item type
 * @param binding viewBinding
 */
abstract class BaseViewHolder<ITEM : Any>(
    val binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: ITEM) // item Bind
}