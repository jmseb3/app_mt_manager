package com.wonddak.mtmanger.ui.adapter

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.ItemPersonlistBinding
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.ui.common.adapter.BaseDataBindingRecyclerAdapter

class PersonRecyclerAdapter(
    private val personListItemCallback: PersonListItemCallback
) :
    BaseDataBindingRecyclerAdapter<Person, ItemPersonlistBinding>(R.layout.item_personlist) {
    interface PersonListItemCallback {
        fun itemClick(item: Person)
        fun itemLongClick(item: Person)
        fun phoneNumberClick(item: Person)
    }


    override fun onBindItem(binding: ItemPersonlistBinding, item: Person) {
        binding.person = item
        binding.itemPersonall.apply {
            setOnClickListener {
                personListItemCallback.itemClick(item)
            }
            setOnLongClickListener {
                personListItemCallback.itemLongClick(item)
                return@setOnLongClickListener true
            }
        }
        binding.itemPersonphone.setOnClickListener {
            personListItemCallback.phoneNumberClick(item)
        }
    }
}