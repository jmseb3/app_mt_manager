package com.wonddak.mtmanger.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @param ITEM list의 들어가는 item type
 * @param binding viewBinding
 */
abstract class BaseRecyclerAdapter<ITEM : Any, B : ViewBinding>(
) : RecyclerView.Adapter<BaseViewHolder<ITEM>>() {

    open var itemList: MutableList<ITEM> = mutableListOf()// 아이템 리스트 ||기본 empty
    abstract fun initViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ITEM>

    //아이템 insert
    open fun insertItems(items: List<ITEM>?) {
        this.itemList.run {
            clear()
            itemList.addAll(items ?: emptyList())
        }
        notifyDataSetChanged()
    }

    open fun addItem(getItem: ITEM) {
        val index = itemList.size
        itemList.add(getItem)
        notifyItemInserted(index)
    }

    // 기본적으로 viewHolder 을 생성하지 않고 자동으로 BaseViewHolder 을 기반으로 생성해준다
    // viewType 에 따라 다르게 보여야한다면 viewHolder 을 BaseViewHolder 상속받아 viewType에 따라 분기하여 만들어주면된다. (override 시켜서 사용)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ITEM> {
        return initViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder<ITEM>, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
