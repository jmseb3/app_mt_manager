package com.wonddak.foodrecipe.ui.common.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

open class BaseViewBindingActivity<B : ViewBinding>(
    val inflate: (LayoutInflater) -> B,
) : CommonActivity() {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

}
