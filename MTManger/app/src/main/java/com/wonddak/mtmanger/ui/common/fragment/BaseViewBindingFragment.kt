package com.wonddak.foodrecipe.ui.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

open class BaseViewBindingFragment<B : ViewBinding>(
    val inflate: (LayoutInflater,ViewGroup?,Boolean) -> B,
) : CommonFragment() {

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflate(layoutInflater,container,false)
        return binding.root
    }

}