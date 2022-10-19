package com.wonddak.foodrecipe.ui.common.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingActivity <B : ViewDataBinding>
    (@LayoutRes private val resId: Int) : CommonActivity() {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, resId)
        binding.lifecycleOwner = this
        initBinding()
    }

    abstract fun initBinding()

}
