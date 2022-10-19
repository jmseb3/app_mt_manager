package com.wonddak.foodrecipe.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * DataBinding 을 이용하는 RecyclerView 기본형
 * @property onBindItem ViewHolder에 아이템별로 연결해주는 abstract fun
 * @param ITEM list의 들어가는 item type
 * @param binding viewBinding
 * @param inflate ViewBinding inflate
 * @sample ExampleViewBindingClass
 * @sample ExampleDataBindingObject
 */
abstract class BaseViewBindingRecyclerAdapter<ITEM : Any, B : ViewBinding>(
    val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B,
) : BaseRecyclerAdapter<ITEM, B>() {
    abstract fun onBindItem(binding: B, item: ITEM) // 아이템 연결

    override fun initViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ITEM> {
        val binding: B = inflate(LayoutInflater.from(parent.context), parent, false)
        return object : BaseViewHolder<ITEM>(binding) {

            override fun onBind(item: ITEM) {
                this@BaseViewBindingRecyclerAdapter.onBindItem(binding, item)
            }
        }
    }
}

